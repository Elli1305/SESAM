package com.gpse.sesam.domain.credential.credentials.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.Credential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredential;
import com.gpse.sesam.domain.credential.credentials.external.ExternalCredentialService;
import com.gpse.sesam.domain.credential.issuing.*;
import com.gpse.sesam.domain.credential.validation.ComparisonRule;
import com.gpse.sesam.domain.credential.validation.LengthRule;
import com.gpse.sesam.domain.credential.validation.RangeRule;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerRepository;
import com.gpse.sesam.web.cmd.*;
import jakarta.validation.Valid;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.ledger.LedgerResults;
import org.hyperledger.indy.sdk.pool.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CredentialServiceImpl implements CredentialService {

    private final WebClient client;

    private final ObjectMapper mapper;

    private static final Map<String, String> agentForDid = Map.of(
            "XgpWt5zepWmbpuRUT82js9", "tlabs",
            "9yGivzVEatBj7o9pNjoFbi", "university"
    );

    private static final Map<String, String> magicCredentialDefinitionIds = Map.of(
            "$T-MEMBER", "XgpWt5zepWmbpuRUT82js9:3:CL:694410:T-MEMBER",
            "$T-TRAINING", "XgpWt5zepWmbpuRUT82js9:3:CL:694412:T-TRAINING",
            "$U-MEMBER", "9yGivzVEatBj7o9pNjoFbi:3:CL:694437:U-MEMBER",
            "$U-TRAINING", "9yGivzVEatBj7o9pNjoFbi:3:CL:694444:U-TRAINING"
    );

    private final IssuerRepository issuerRepository;
    private final CredentialRepository credentialRepository;

    private final LocationService locationService;

    private final ExternalCredentialService externalCredentialService;

    private final CategoryService categoryService;
    private final Pool pool;

    @Autowired
    public CredentialServiceImpl(final WebClient client, final ObjectMapper mapper, final Pool pool,
                                 final CredentialRepository credentialRepository,
                                 final IssuerRepository issuerRepository,
                                 final LocationService locationService,
                                 final ExternalCredentialService externalCredentialService,
                                 CategoryService categoryService) {
        this.client = client;
        this.mapper = mapper;
        this.pool = pool;
        this.issuerRepository = issuerRepository;
        this.credentialRepository = credentialRepository;
        this.locationService = locationService;
        this.externalCredentialService = externalCredentialService;
        this.categoryService = categoryService;
    }

    private String replaceMagicCredentialDefinitionIds(String credentialDefinitionId) {
        return magicCredentialDefinitionIds.getOrDefault(credentialDefinitionId, credentialDefinitionId);
    }

    @SuppressWarnings("CPD-START")
    @Override
    public List<InternalCredential> getCredentials() {
        final List<InternalCredential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        return credentials;
    }

    @Override
    public List<Credential> getAllCredentials() {
        final List<Credential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        credentials.addAll(externalCredentialService.getExternalCredentials());
        return credentials;
    }

    @Override
    public List<InternalCredential> getCredentialsByIssuerId(final Long id) {
        final Issuer issuer = issuerRepository.findById(String.valueOf(id)).orElseThrow();
        return issuer.getCredentials();
    }

    @Override
    public List<Credential> getCredentialByCredentialDefinitionId(final String id) {
        List<Credential> credential = new ArrayList<>(credentialRepository.findAllByCredentialDefinitionId(id));
        if (credential.isEmpty()) {
            credential.addAll(externalCredentialService.getExternalCredentialByCredentialDefinitionId(id));
        }
        return credential;
    }


    @Override
    public Optional<InternalCredential> getCredential(final Long id) {
        return credentialRepository.findById(id);
    }

    private String sendCredentialIssueRequest(@Valid final IssueCredentialRequest issueCredentialRequest)
            throws JsonProcessingException {
        return client.post().uri("credential/issue").contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON).bodyValue(mapper.writeValueAsString(issueCredentialRequest))
                .retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(5000)).block();
    }

    @Override
    public String issueCredential(final Long id, final List<IssueCredentialAttributeCmd> attributeCmds)
            throws JsonProcessingException {
        final InternalCredential credential = credentialRepository.findById(id).orElseThrow();

        final Map<Long, IssueCredentialAttributeCmd> attributeCmdMap = attributeCmds.stream()
                .collect(Collectors.toMap(IssueCredentialAttributeCmd::id, Function.identity()));

        final List<IssueCredentialAttribute> attributes = credential.getForm().stream().map(entry -> {
            final IssueCredentialAttributeCmd correspondingAttributeCmd = attributeCmdMap.get(entry.getId());
            if (correspondingAttributeCmd == null) {
                return null;
            }
            boolean isValid = entry.getValidationRules().stream().allMatch(rule -> {
                if (rule instanceof ComparisonRule) {
                    if (((ComparisonRule) rule).isCompareWithAttribute()) {
                        Long chosenAttributeId = credential.getForm().stream()
								.filter(a -> ((ComparisonRule) rule).getAttributeName().equals(a.getLabel()))
								.toList().get(0).getId();
                        ((ComparisonRule) rule).setContent(attributeCmdMap.get(chosenAttributeId).value());
                    }
                } else if (rule instanceof RangeRule) {
                    if (((RangeRule) rule).isCompareWithAttribute()) {
                        Long chosenAttributeFromId = credential.getForm().stream()
								.filter(a -> ((RangeRule) rule).getAttributeNameFrom().equals(a.getLabel()))
								.toList().get(0).getId();
                        Long chosenAttributeToId = credential.getForm().stream()
								.filter(a -> ((RangeRule) rule).getAttributeNameTo().equals(a.getLabel()))
								.toList().get(0).getId();
                        ((RangeRule) rule).setValueFrom(attributeCmdMap.get(chosenAttributeFromId).value());
                        ((RangeRule) rule).setValueTo(attributeCmdMap.get(chosenAttributeToId).value());
                    }
                } else if (rule instanceof LengthRule) {
                    if (((LengthRule) rule).isCompareWithAttribute()) {
                        Long chosenAttributeId = credential.getForm().stream()
								.filter(a -> ((LengthRule) rule).getAttributeName().equals(a.getLabel()))
								.toList().get(0).getId();
                        ((LengthRule) rule).setLength(attributeCmdMap.get(chosenAttributeId).value().length());
                    }
                }
                return rule.validate(correspondingAttributeCmd.value(), entry.getType());
            });
            if (!isValid) {
                throw new IllegalArgumentException("Input " + correspondingAttributeCmd.value()
						+ " for attribute " + entry.getAttributeName() + " is not valid");
            }

            return new IssueCredentialAttribute(entry.getAttributeName(), entry.getType() == FormEntryType.DATE
                    ? correspondingAttributeCmd.value().replace("-", "")
                    : correspondingAttributeCmd.value(), entry.getType());
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (credential.getForm().size() != attributes.size()) {
            return null;
        }

        return sendCredentialIssueRequest(new IssueCredentialRequest(credential.getAgent(),
                new IssueCredential(credential.getCredentialDefinitionId(), attributes)));
    }

    @Override
    public void saveAll(final Iterable<InternalCredential> credentials) {
        credentialRepository.saveAll(credentials);
    }

    @Override
    public List<CredentialCmd> getCredentialByLocation(Long id) {

        final Location location = locationService.getLocation(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " does not exist"));

        final Iterable<InternalCredential> credentials = getCredentialFromAttachedProofConfig(location);

        final List<CredentialCmd> cmds = new ArrayList<>();

        for (final InternalCredential credential : credentials) {

            final String credentialName = credential.getName();
            final List<String> externalCredentials = new ArrayList<>();
            String categoryName = "";
            if (credential.getCategory() != null) {
                categoryName = credential.getCategory().getName();
                for (final ExternalCredential externalCredential : credential.getCategory().getExternalCredentials()) {
                    final String external = externalCredential.getName();
                    externalCredentials.add(external);
                }
            }
            final List<String> issuers = new ArrayList<>();
            final List<String> rooms = new ArrayList<>();
            for (final Issuer issuer : credential.getIssuer()) {
                final String issuerName = issuer.getFirstName() + " " + issuer.getLastName();
                final String room = issuer.getRoom().getName();
                issuers.add(issuerName);
                rooms.add(room);
            }
            cmds.add(new CredentialCmd(categoryName, credentialName, externalCredentials, issuers, rooms));
        }

        return cmds;
    }

    private Iterable<InternalCredential> getCredentialFromAttachedProofConfig(Location location) {
        return location
                .getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .flatMap(floor -> floor.getRooms().stream())
                .flatMap(room -> room.getDoors().stream())
                .flatMap(door -> Stream.concat(door.getProofConfigIn().stream(), door.getProofConfigOut()
                        .stream()))
                .flatMap(proofConfig -> {
                    final Stream<String> attributeFilterStream = proofConfig.getRequestedPredicates().values()
                            .stream()
                            .flatMap(proofPredicateInfo -> proofPredicateInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    final Stream<String> attributeFilterStream1 = proofConfig.getRequestedAttributes().values()
                            .stream()
                            .flatMap(proofAttributeInfo -> proofAttributeInfo.getRestrictions().stream())
                            .map(AttributeFilter::getCredentialDefinitionId);
                    return Stream.concat(attributeFilterStream, attributeFilterStream1);
                })
                .filter(Objects::nonNull)
                .flatMap(definitionId -> credentialRepository.findAllByCredentialDefinitionId(definitionId)
                        .stream())
                .collect(Collectors.toSet());
    }

    @Override
    public void create(final CreateCredentialCmd createCredentialCmd) {
        final InternalCredential credential = new InternalCredential(
                createCredentialCmd.getName(),
                replaceMagicCredentialDefinitionIds(createCredentialCmd.getCredentialDefinitionId()),
                createCredentialCmd.getAgent(),
                createCredentialCmd.getAttributes().stream()
                        .map(createAttributeCmd ->
                                new FormEntry(
                                        createAttributeCmd.getName(),
                                        createAttributeCmd.getType(),
                                        createAttributeCmd.getAttributeName(),
                                        createAttributeCmd.getValidationRules()
                                )
                        )
                        .toList(),
                createCredentialCmd.getConditions().stream()
                        .map(createConditionCmd -> new ChecklistEntry(createConditionCmd.getLabel()))
                        .toList()
        );

        credentialRepository.save(credential);
    }

    @Override
    public void delete(final Long id) {
        final Optional<InternalCredential> optionalCredential = credentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final InternalCredential credential = optionalCredential.get();

        credential.setCategory(null);

        for (final Issuer issuer : credential.getIssuer()) {
            issuer.setCredentials(
                    issuer.getCredentials()
                            .stream()
                            .filter(credential1 -> !credential1.getId().equals(credential.getId()))
                            .collect(Collectors.toList())
            );
        }

        credential.setIssuer(null);

        credentialRepository.deleteById(id);
    }

    @Override
    public void update(final Long id, final UpdateCredentialCmd updateCredentialCmd) {
        final Optional<InternalCredential> optionalCredential = credentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final InternalCredential credential = optionalCredential.get();

        credential.setName(updateCredentialCmd.getName());
        credential.setAgent(updateCredentialCmd.getAgent());
        credential.setCredentialDefinitionId(
                replaceMagicCredentialDefinitionIds(
                        updateCredentialCmd.getCredentialDefinitionId()
                )
        );

        final List<FormEntry> formEntries = new ArrayList<>();

        for (final UpdateAttributeCmd attribute : updateCredentialCmd.getAttributes()) {
            formEntries.add(
                    new FormEntry(
                            attribute.getName(),
                            attribute.getType(),
                            attribute.getAttributeName(),
                            attribute.getValidationRules()
                    )
            );
        }

        credential.setForm(formEntries);

        final List<ChecklistEntry> checklist = new ArrayList<>();

        for (final UpdateConditionCmd condition : updateCredentialCmd.getConditions()) {
            checklist.add(
                    new ChecklistEntry(
                            condition.getLabel()
                    )
            );
        }

        credential.setChecklist(checklist);

        credentialRepository.save(credential);
    }

    @Override
    public List<CredentialCmd> getAllCredentialsForView() {
        List<InternalCredential> credentials = getCredentials();
        List<Category> categories = categoryService.getCategory();
        List<CredentialCmd> cmd = new ArrayList<>();

        for (InternalCredential credential : credentials) {
            String categoryName = "-";
            List<String> externalCredentials = new ArrayList<>();
            List<String> issuers = new ArrayList<>();
            List<String> room = new ArrayList<>();
            for (Category category : categories) {
                if (category.getCredentials().contains(credential)) {
                    categoryName = category.getName();
                    for (ExternalCredential external : credential.getCategory().getExternalCredentials()) {
                        externalCredentials.add(external.getName());
                    }
                }
            }

            String credentialName = credential.getName();
            if (!credential.getIssuer().isEmpty()) {
                for (Issuer issuer : credential.getIssuer()) {
                    issuers.add(issuer.getFirstName() + " " + issuer.getLastName());
                    String roomName = "-";
                    if (!(issuer.getRoom() == null)) {
                        roomName = issuer.getRoom().getName();
                    }
                    room.add(roomName);
                }
            }
            cmd.add(new CredentialCmd(categoryName, credentialName, externalCredentials, issuers, room));

        }

        return cmd;
    }

    @Override
    public List<AllCredentialCmd> getAllForView() {
        List<InternalCredential> credentials = getCredentials();
        List<Category> categories = categoryService.getCategory();
        List<AllCredentialCmd> cmd = new ArrayList<>();

        for (InternalCredential credential : credentials) {
            String categoryName = "-";
            List<String> externalCredentials = new ArrayList<>();
            List<String> issuers = new ArrayList<>();
            List<String> room = new ArrayList<>();
            for (Category category : categories) {
                if (category.getCredentials().contains(credential)) {
                    categoryName = category.getName();
                    for (ExternalCredential external : credential.getCategory().getExternalCredentials()) {
                        externalCredentials.add(external.getName());
                    }
                }
            }

            String credentialName = credential.getName();
            if (!credential.getIssuer().isEmpty()) {
                for (Issuer issuer : credential.getIssuer()) {
                    issuers.add(issuer.getFirstName() + " " + issuer.getLastName());
                    String roomName = "-";
                    if (!(issuer.getRoom() == null)) {
                        roomName = issuer.getRoom().getName();
                    }
                    room.add(roomName);
                }
            }
            cmd.add(new AllCredentialCmd(categoryName, credentialName, "Intern", externalCredentials, issuers,
                    room));
        }
        List<ExternalCredential> externalCredentials = externalCredentialService.getExternalCredentials();
        for (ExternalCredential extern : externalCredentials) {
            List<String> intern = new ArrayList<>();
            String categoryName = "-";
            String name = extern.getName();
            for (Category category : categories) {
                if (category.getExternalCredentials().contains(extern)) {
                    categoryName = category.getName();
                    for (InternalCredential internal : category.getCredentials()) {
                        intern.add(internal.getName());
                    }
                }

            }
            List<String> room = new ArrayList<>();
            List<String> issuer = new ArrayList<>();
            cmd.add(new AllCredentialCmd(categoryName, name, "Extern", intern, issuer, room));

        }
        return cmd;
    }

    @Override
    public List<AllCredentialCmd> getAllCredentialsByLocation(Long id) {
        List<AllCredentialCmd> cmds = new ArrayList<>();
        List<CredentialCmd> intern = getCredentialByLocation(id);
        List<ExternalCredentialCmd> extern = externalCredentialService.getAllExternalByLocation(id);

        for (CredentialCmd in : intern) {
            cmds.add(new AllCredentialCmd(in.getCategoryName(), in.getCredentialName(), "Intern",
                    in.getExternalCredential(), in.getIssuerName(), in.getRoom()));
        }

        for (ExternalCredentialCmd ex : extern) {
            cmds.add(new AllCredentialCmd(ex.getCategoryName(), ex.getCredentialName(), "Extern",
                    ex.getInternalCredential(), new ArrayList<>(), new ArrayList<>()));
        }

        return cmds;
    }

    @Override
    public CredentialSchemaCmd getCredentialSchema(String credentialDefinitionId) throws IndyException, ExecutionException, InterruptedException, JsonProcessingException {
        String normalizedCredentialDefinitionId = replaceMagicCredentialDefinitionIds(credentialDefinitionId);
        String getCredDefRequest = Ledger.buildGetCredDefRequest(null, normalizedCredentialDefinitionId).get();
        String getCredDefResponse = Ledger.submitRequest(pool, getCredDefRequest).get();

        JsonNode getCredDefResponseNode = mapper.readTree(getCredDefResponse);

        LedgerResults.ParseResponseResult getCredDefResponseResult = Ledger.parseGetCredDefResponse(getCredDefResponse).get();

        JsonNode getCredDefResponseResultNode = mapper.readTree(getCredDefResponseResult.getObjectJson());

        String getTxnRequest = Ledger.buildGetTxnRequest(null, "DOMAIN", getCredDefResponseResultNode.get("schemaId").asInt()).get();
        String getTxnResponse = Ledger.submitRequest(pool, getTxnRequest).get();

        JsonNode getTxnResponseNode = mapper.readTree(getTxnResponse);

        ArrayNode attrNamesNode = (ArrayNode) getTxnResponseNode.get("result")
                .get("data")
                .get("txn")
                .get("data")
                .get("data")
                .get("attr_names");

        List<String> attrNames = mapper.convertValue(attrNamesNode, new TypeReference<>() {
        });

        return new CredentialSchemaCmd(
                getCredDefResponseResultNode.get("tag").asText(),
                normalizedCredentialDefinitionId,
                agentForDid.get(getCredDefResponseNode.get("result").get("origin").asText()),
                getCredDefResponseResultNode.get("ver").asText(),
                attrNames
        );

    }
}
