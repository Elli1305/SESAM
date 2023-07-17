package com.gpse.sesam.domain.credential.credentials.external;

import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issue.issuing.FormEntry;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.web.cmd.CreateAttributeCmd;
import com.gpse.sesam.web.cmd.CreateExternalCredentialCmd;
import com.gpse.sesam.web.cmd.ExternalCredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gpse.sesam.domain.credential.credentials.internal.CredentialServiceImpl.replaceMagicCredentialDefinitionIds;

/**
 * Implementierung des ExternalCredentialService, der Operationen zur Verwaltung von externen Credentials durchführt.
 */
@Service
public class ExternalCredentialServiceImpl implements ExternalCredentialService {
    private final ExternalCredentialRepository externalCredentialRepository;

    private final LocationService locationService;

    private final CategoryService categoryService;

    /**
     * Konstruktor für die ExternalCredentialServiceImpl-Klasse.
     *
     * @param externalCredentialRepository Das ExternalCredentialRepository.
     * @param locationService              Der LocationService.
     * @param categoryService              Der CategoryService.
     */

    @Autowired
    public ExternalCredentialServiceImpl(final ExternalCredentialRepository externalCredentialRepository,
                                         final LocationService locationService,
                                         final CategoryService categoryService) {
        this.externalCredentialRepository = externalCredentialRepository;
        this.locationService = locationService;
        this.categoryService = categoryService;
    }

    /**
     * Ruft alle externen Credentials ab.
     *
     * @return Eine Liste aller vorhandenen externen Credentials.
     */
    @SuppressWarnings("CPD-START")
    @Override
    public List<ExternalCredential> getExternalCredentials() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();
        externalCredentialRepository.findAll().forEach(externalCredentials::add);
        return externalCredentials;
    }

    /**
     * Ruft externe Credentials anhand der angegebenen ID ab.
     *
     * @param id Die ID der externen Credentials.
     * @return Die gefundenen externen Credentials oder Optional.empty(), wenn keine externen Credentials mit der ID
     * vorhanden sind.
     */
    @Override
    public Optional<ExternalCredential> getExternalCredential(final Long id) {
        return externalCredentialRepository.findById(id);
    }

    @Override
    public void createExternalCredential(final CreateExternalCredentialCmd createExternalCredentialCmd) {
        final ExternalCredential credential = new ExternalCredential(
                createExternalCredentialCmd.getName(),
                createExternalCredentialCmd.getVersion(),
                replaceMagicCredentialDefinitionIds(createExternalCredentialCmd.getCredentialDefinitionId()),
                createExternalCredentialCmd.getAttributes().stream()
                        .map(createAttributeCmd ->
                                new FormEntry(
                                        createAttributeCmd.getName(),
                                        createAttributeCmd.getType(),
                                        createAttributeCmd.getAttributeName(),
                                        createAttributeCmd.getValidationRules()
                                )
                        )
                        .toList()
        );

        externalCredentialRepository.save(credential);
    }

    @Override
    public void deleteExternalCredential(final Long id) {
        final Optional<ExternalCredential> optionalCredential = externalCredentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final ExternalCredential credential = optionalCredential.get();
        final Category category = credential.getCategory();

        if (category != null) {
            category.removeExternalCredential(credential);
            credential.setCategory(null);
        }

        externalCredentialRepository.deleteById(id);
    }

    @Override
    public void updateExternalCredential(final Long id, final CreateExternalCredentialCmd createExternalCredentialCmd) {
        final Optional<ExternalCredential> optionalCredential = externalCredentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final ExternalCredential credential = optionalCredential.get();

        credential.setName(createExternalCredentialCmd.getName());
        credential.setVersion(createExternalCredentialCmd.getVersion());
        credential.setCredentialDefinitionId(
                replaceMagicCredentialDefinitionIds(
                        createExternalCredentialCmd.getCredentialDefinitionId()
                )
        );

        final List<FormEntry> formEntries = new ArrayList<>();

        for (final CreateAttributeCmd attribute : createExternalCredentialCmd.getAttributes()) {
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

        externalCredentialRepository.save(credential);
    }

    /**
     * Ruft externe Credentials anhand der angegebenen Credential-Definition-ID ab.
     *
     * @param id Die Credential-Definition-ID.
     * @return Eine Liste der gefundenen externen Credentials.
     */
    @Override
    public List<ExternalCredential> getExternalCredentialByCredentialDefinitionId(final String id) {
        return externalCredentialRepository.findAllByCredentialDefinitionId(id);
    }

    /**
     * Ruft alle externen Credentials als ExternalCredentialCmd-Objekte ab.
     *
     * @return Eine Liste von ExternalCredentialCmd-Objekten, die alle externen Credentials repräsentieren.
     */
    @Override
    public List<ExternalCredentialCmd> getAllExternal() {
        final List<ExternalCredential> externalCredentials = getExternalCredentials();
        final List<ExternalCredentialCmd> cmds = new ArrayList<>();
        final List<Category> categories = categoryService.getCategory();
        for (final ExternalCredential extern : externalCredentials) {
            final List<String> intern = new ArrayList<>();
            String categoryName = "-";
            final String name = extern.getName();
            for (final Category category : categories) {
                if (category.getExternalCredentials().contains(extern)) {
                    categoryName = category.getName();
                    for (final InternalCredential internal : category.getCredentials()) {
                        intern.add(internal.getName());
                    }
                }

            }
            cmds.add(new ExternalCredentialCmd(categoryName, name, intern));

        }
        return cmds;
    }

    /**
     * Ruft alle externen Credentials anhand der Proof-Configs an einem Standort ab
     *
     * @param location Standort zum Abrufen der ProofConfigs zum Erhalten der externen Credentials
     */

    private Iterable<ExternalCredential> getCredentialFromAttachedProofConfig(final Location location) {
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
                .flatMap(definitionId -> externalCredentialRepository.findAllByCredentialDefinitionId(definitionId)
                        .stream())
                .collect(Collectors.toSet());
    }

    /**
     * Ruft alle externen Credentials anhand der Standort-ID als ExternalCredentialCmd-Objekte ab.
     *
     * @param id Die ID des Standorts.
     * @return Eine Liste von ExternalCredentialCmd-Objekten, die alle externen Credentials für den Standort
     * repräsentieren.
     */
    @Override
    public List<ExternalCredentialCmd> getAllExternalByLocation(final Long id) {
        final List<Category> categories = categoryService.getCategory();
        final Location location = locationService.getLocation(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " does not exist"));

        final Iterable<ExternalCredential> credentials = getCredentialFromAttachedProofConfig(location);

        final List<ExternalCredentialCmd> cmds = new ArrayList<>();

        for (final ExternalCredential credential : credentials) {

            final String credentialName = credential.getName();
            final List<String> internalCredentials = new ArrayList<>();
            String categoryName = "";
            for (final Category category : categories) {
                if (category.getExternalCredentials().contains(credential)) {
                    categoryName = category.getName();
                    for (final InternalCredential internal : category.getCredentials()) {
                        internalCredentials.add(internal.getName());
                    }
                }

            }
            cmds.add(new ExternalCredentialCmd(categoryName, credentialName, internalCredentials));
        }

        return cmds;

    }

    /**
     * Löscht alle externen Credentials.
     */
    @Override
    public void deleteAll() {
        externalCredentialRepository.deleteAll();
    }

    /**
     * Speichert eine Liste von externen Credentials.
     *
     * @param externalCredentials Eine Iterable-Liste von externen Credentials.
     */
    @Override
    public void saveAll(final Iterable<ExternalCredential> externalCredentials) {
        externalCredentialRepository.saveAll(externalCredentials);
    }

    @Override
    public void save(final ExternalCredential externalCredential) {
        externalCredentialRepository.save(externalCredential);
    }
}
