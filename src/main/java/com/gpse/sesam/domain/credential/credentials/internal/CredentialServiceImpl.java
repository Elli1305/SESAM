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
import com.gpse.sesam.domain.credential.issue.issuing.*;
import com.gpse.sesam.domain.credential.issue.validation.ComparisonRule;
import com.gpse.sesam.domain.credential.issue.validation.LengthRule;
import com.gpse.sesam.domain.credential.issue.validation.RangeRule;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.domain.user.issuer.Issuer;
import com.gpse.sesam.domain.user.issuer.IssuerRepository;
import com.gpse.sesam.web.cmd.*;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import org.hyperledger.indy.sdk.IndyException;
import org.hyperledger.indy.sdk.LibIndy;
import org.hyperledger.indy.sdk.ledger.Ledger;
import org.hyperledger.indy.sdk.ledger.LedgerResults;
import org.hyperledger.indy.sdk.pool.Pool;
import org.hyperledger.indy.sdk.pool.PoolJSONParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementierung des CredentialService, der Operationen zur Verwaltung von internen Credentials durchführt.
 */
@Service
public class CredentialServiceImpl implements CredentialService {
    private static final Map<String, String> AGENT_FOR_DID = Map.of(
            "XgpWt5zepWmbpuRUT82js9", "tlabs",
            "9yGivzVEatBj7o9pNjoFbi", "university"
    );

    private static final Map<String, String> MAGIC_CREDENTIAL_DEFINITION_IDS = Map.of(
            "$T-MEMBER", "XgpWt5zepWmbpuRUT82js9:3:CL:694410:T-MEMBER",
            "$T-TRAINING", "XgpWt5zepWmbpuRUT82js9:3:CL:694412:T-TRAINING",
            "$U-MEMBER", "9yGivzVEatBj7o9pNjoFbi:3:CL:694437:U-MEMBER",
            "$U-TRAINING", "9yGivzVEatBj7o9pNjoFbi:3:CL:694444:U-TRAINING"
    );
    private static final String DEFAULT_POOL_NAME = "default_pool";

    private final WebClient client;

    private final ObjectMapper mapper;

    private final IssuerRepository issuerRepository;

    private final CredentialRepository credentialRepository;

    private final LocationService locationService;

    private final ExternalCredentialService externalCredentialService;

    private final CategoryService categoryService;

    private Pool pool = null;


    /**
     * Konstruktor für die CredentialServiceImpl-Klasse.
     *
     * @param client                     Der WebClient für HTTP-Anfragen.
     * @param mapper                     Der ObjectMapper für JSON-Serialisierung und -Deserialisierung.
     * @param credentialRepository       Das CredentialRepository.
     * @param issuerRepository           Das IssuerRepository.
     * @param locationService            Der LocationService.
     * @param externalCredentialService  Der ExternalCredentialService.
     * @param categoryService            Der CategoryService.
     */
    @Autowired
    public CredentialServiceImpl(final WebClient client, final ObjectMapper mapper,
                                 final CredentialRepository credentialRepository,
                                 final IssuerRepository issuerRepository,
                                 final LocationService locationService,
                                 final ExternalCredentialService externalCredentialService,
                                 final CategoryService categoryService) {
        this.client = client;
        this.mapper = mapper;
        this.issuerRepository = issuerRepository;
        this.credentialRepository = credentialRepository;
        this.locationService = locationService;
        this.externalCredentialService = externalCredentialService;
        this.categoryService = categoryService;
    }

    /**
     * Ersetzt magische Credential-Definition-IDs durch ihre tatsächlichen Werte.
     *
     * @param credentialDefinitionId die Credential-Definition-ID, die ersetzt werden soll
     * @return die ersetzte Credential-Definition-ID
     */
    public static String replaceMagicCredentialDefinitionIds(String credentialDefinitionId) {
        return MAGIC_CREDENTIAL_DEFINITION_IDS.getOrDefault(credentialDefinitionId, credentialDefinitionId);
    }

    /**
     * Zerstört den Pool und schließt die Verbindung.
     *
     * @throws IndyException          wenn ein Fehler in der Indy-Bibliothek auftritt
     * @throws ExecutionException    wenn ein Fehler bei der Ausführung auftritt
     * @throws InterruptedException  wenn der Thread während des Wartens unterbrochen wird
     */
    @PreDestroy
    private void destroy() throws IndyException, ExecutionException, InterruptedException {
        if (pool == null) {
            return;
        }

        pool.closePoolLedger().get();
        pool.close();

        pool = null;
    }

    /**
     * Erstellt und öffnet einen Pool.
     *
     * @return der erstellte und geöffnete Pool
     * @throws FileNotFoundException wenn die Datei nicht gefunden wird
     * @throws IndyException          wenn ein Fehler in der Indy-Bibliothek auftritt
     * @throws ExecutionException    wenn ein Fehler bei der Ausführung auftritt
     * @throws InterruptedException  wenn der Thread während des Wartens unterbrochen wird
     */
    private Pool createPool() throws FileNotFoundException, IndyException, ExecutionException, InterruptedException {
        if (!LibIndy.isInitialized()) {
            LibIndy.init();
        }

        if (LibIndy.api == null) {
            return null;
        }

        File genesisTxnFile = ResourceUtils.getFile("classpath:test.bcovrin.vonx.io.jsonl");

        PoolJSONParameters.CreatePoolLedgerConfigJSONParameter createPoolLedgerConfigJSONParameter =
                new PoolJSONParameters.CreatePoolLedgerConfigJSONParameter(genesisTxnFile.getAbsolutePath());

        Pool.setProtocolVersion(2).get();

        try {
            Pool.createPoolLedgerConfig(DEFAULT_POOL_NAME, createPoolLedgerConfigJSONParameter.toJson()).get();
        } catch (ExecutionException | IndyException | InterruptedException ignored) {
            // Intentionally ignored.
        }


        return Pool.openPoolLedger(DEFAULT_POOL_NAME, "{}").get();
    }


    /**
     * Ruft alle internen Credentials ab.
     *
     * @return Eine Liste aller vorhandenen internen Credentials.
     */
    @SuppressWarnings("CPD-START")
    @Override
    public List<InternalCredential> getCredentials() {
        final List<InternalCredential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        return credentials;
    }

    /**
     * Ruft alle Credentials (interne und externe) ab.
     *
     * @return Eine Liste aller vorhandenen Credentials.
     */
    @Override
    public List<Credential> getAllCredentials() {
        final List<Credential> credentials = new ArrayList<>();
        credentialRepository.findAll().forEach(credentials::add);
        credentials.addAll(externalCredentialService.getExternalCredentials());
        return credentials;
    }

    /**
     * Ruft die internen Credentials anhand der Issuer-ID ab.
     *
     * @param id Die ID des Issuers.
     * @return Eine Liste der gefundenen internen Credentials für den Issuer.
     */

    @Override
    public List<InternalCredential> getCredentialsByIssuerId(final Long id) {
        final Issuer issuer = issuerRepository.findById(String.valueOf(id)).orElseThrow();
        return issuer.getCredentials();
    }


    /**
     * Ruft die Credentials anhand der Credential-Definition-ID ab.
     *
     * @param id Die Credential-Definition-ID.
     * @return Eine Liste der gefundenen Credentials.
     */
    @Override
    public List<Credential> getCredentialByCredentialDefinitionId(final String id) {
        List<Credential> credential = new ArrayList<>(credentialRepository.findAllByCredentialDefinitionId(id));
        if (credential.isEmpty()) {
            credential.addAll(externalCredentialService.getExternalCredentialByCredentialDefinitionId(id));
        }
        return credential;
    }


    /**
     * Ruft die interne Credential anhand der angegebenen ID ab.
     *
     * @param id Die ID der internen Credential.
     * @return Die gefundene interne Credential oder Optional.empty(), wenn keine interne Credential mit der ID
     * vorhanden ist.
     */
    @Override
    public Optional<InternalCredential> getCredential(final Long id) {
        return credentialRepository.findById(id);
    }

    /**
     * Sendet eine Anforderung zur Ausstellung eines Credentials.
     *
     * @param issueCredentialRequest die IssueCredentialRequest, die die Ausstellungsanforderung enthält
     * @return die Antwort als String
     * @throws JsonProcessingException wenn ein Fehler bei der Verarbeitung von JSON-Daten auftritt
     */
    private String sendCredentialIssueRequest(@Valid final IssueCredentialRequest issueCredentialRequest)
            throws JsonProcessingException {
        return client.post().uri("credential/issue").contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).bodyValue(mapper.writeValueAsString(issueCredentialRequest))
                .retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(5000)).block();
    }

    /**
     * Sendet eine Anfrage zum Ausstellen einer Credential.
     *
     * @param id                Die ID der internen Credential.
     * @param attributeCmds     Die Liste der Attribut-Commands für die Ausstellung der Credential.
     * @return Die Antwort auf die Ausstellungsanfrage.
     * @throws JsonProcessingException Falls ein Fehler bei der JSON-Verarbeitung auftritt.
     */
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

    /**
     * Speichert eine Liste von internen Credentials.
     *
     * @param credentials Eine Iterable-Liste von internen Credentials.
     */
    @Override
    public void saveAll(final Iterable<InternalCredential> credentials) {
        credentialRepository.saveAll(credentials);
    }

    /**
     * Ruft die Credentials anhand des Standorts ab.
     *
     * @param id Die ID des Standorts.
     * @return Eine Liste der gefundenen Credentials für den Standort.
     */
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

    /**
     * Ruft alle intern Credentials anhand der Proof-Configs an einem Standort ab
     *
     * @param location Standort zum Abrufen der ProofConfigs zum Erhalten der internen Credentials
     */
    private Iterable<InternalCredential> getCredentialFromAttachedProofConfig(Location location) {
        return location
                .getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .flatMap(floor -> floor.getRooms().stream())
                .flatMap(room -> room.getDoors().stream())
                .flatMap(door -> door.getDoorConfigs().stream())
                .flatMap(twoWayDoorConfig -> Stream.of(twoWayDoorConfig.getProofConfigIn(),
                        twoWayDoorConfig.getProofConfigOut()))
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

    /**
     * Erstellt eine neue Credential.
     *
     * @param createCredentialCmd Das CreateCredentialCmd-Objekt, das die Informationen für die Erstellung der
     *                            Credential enthält.
     */
    @Override
    public void create(boolean createOnLedger, CreateCredentialCmd createCredentialCmd) throws JsonProcessingException {
        String credentialDefinitionId =
                replaceMagicCredentialDefinitionIds(createCredentialCmd.getCredentialDefinitionId());

        if (createOnLedger) {
            final DeployCredentialCmd deployCredentialCmd = new DeployCredentialCmd(
                    createCredentialCmd.getAgent(),
                    createCredentialCmd.getName(),
                    new DeployCredentialTemplateCmd(
                            createCredentialCmd.getName(),
                            createCredentialCmd.getVersion(),
                            createCredentialCmd
                                    .getAttributes()
                                    .stream()
                                    .map(CreateAttributeCmd::getAttributeName)
                                    .collect(Collectors.toList())
                    )
            );

            credentialDefinitionId = client.post().uri("credential/deploy").contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_PLAIN).bodyValue(mapper.writeValueAsString(deployCredentialCmd))
                    .retrieve().bodyToMono(String.class).timeout(Duration.ofMillis(30000)).block();
        }

        final InternalCredential credential = new InternalCredential(
                createCredentialCmd.getName(),
                createCredentialCmd.getVersion(),
                credentialDefinitionId,
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

    /**
     * Löscht die Credential mit der angegebenen ID.
     *
     * @param id Die ID der Credential, die gelöscht werden soll.
     */
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

    /**
     * Aktualisiert die Credential mit der angegebenen ID.
     *
     * @param id                   Die ID der Credential, die aktualisiert werden soll.
     * @param updateCredentialCmd  Das UpdateCredentialCmd-Objekt, das die neuen Informationen für die Aktualisierung
     *                             der Credential enthält.
     */
    @Override
    public void update(final Long id, final UpdateCredentialCmd updateCredentialCmd) {
        final Optional<InternalCredential> optionalCredential = credentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final InternalCredential credential = optionalCredential.get();

        credential.setName(updateCredentialCmd.getName());
        credential.setAgent(updateCredentialCmd.getAgent());
        credential.setVersion(updateCredentialCmd.getVersion());
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

    /**
     * Ruft alle Credentials für die Credentialansicht ab.
     *
     * @return Eine Liste von CredentialCmd-Objekten, die alle Credentials für die Credentialansicht repräsentieren.
     */
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

    /**
     * Ruft alle Credentials für die Credentialansicht ab.
     *
     * @return Eine Liste von AllCredentialCmd-Objekten, die alle Credentials für die Credentialansicht repräsentieren.
     */
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

    /**
     * Ruft alle Credentials für den angegebenen Standort ab.
     *
     * @param id Die ID des Standorts.
     * @return Eine Liste von AllCredentialCmd-Objekten, die alle Credentials für den Standort repräsentieren.
     */
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

    /**
     * Ruft das Credential-Schema für die angegebene Credential-Definition-ID ab.
     *
     * @param credentialDefinitionId die ID der Credential-Definition
     * @return das Credential-Schema als CredentialSchemaCmd-Objekt
     * @throws IndyException               wenn ein Fehler in der Indy-Bibliothek auftritt
     * @throws ExecutionException         wenn ein Fehler bei der Ausführung auftritt
     * @throws InterruptedException       wenn der Thread während des Wartens unterbrochen wird
     * @throws JsonProcessingException     wenn ein Fehler bei der Verarbeitung von JSON-Daten auftritt
     * @throws FileNotFoundException     wenn die Datei nicht gefunden wird
     */
    @Override
    public CredentialSchemaCmd getCredentialSchema(String credentialDefinitionId) throws IndyException,
            ExecutionException, InterruptedException, JsonProcessingException, FileNotFoundException {
        if (pool == null) {
            pool = createPool();
        }

        String normalizedCredentialDefinitionId = replaceMagicCredentialDefinitionIds(credentialDefinitionId);
        String getCredDefRequest = Ledger.buildGetCredDefRequest(null, normalizedCredentialDefinitionId)
                .get();
        String getCredDefResponse = Ledger.submitRequest(pool, getCredDefRequest).get();

        JsonNode getCredDefResponseNode = mapper.readTree(getCredDefResponse);

        LedgerResults.ParseResponseResult getCredDefResponseResult = Ledger.parseGetCredDefResponse(getCredDefResponse)
                .get();

        JsonNode getCredDefResponseResultNode = mapper.readTree(getCredDefResponseResult.getObjectJson());

        String getTxnRequest = Ledger.buildGetTxnRequest(
                null,
                "DOMAIN",
                getCredDefResponseResultNode.get("schemaId").asInt()
        ).get();
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
                AGENT_FOR_DID.get(getCredDefResponseNode.get("result").get("origin").asText()),
                getCredDefResponseResultNode.get("ver").asText(),
                attrNames
        );
    }
}
