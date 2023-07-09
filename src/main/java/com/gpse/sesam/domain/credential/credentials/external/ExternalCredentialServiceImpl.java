package com.gpse.sesam.domain.credential.credentials.external;

import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.credential.issuing.FormEntry;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
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
    public ExternalCredentialServiceImpl(ExternalCredentialRepository externalCredentialRepository,
                                         LocationService locationService, CategoryService categoryService) {
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
    public Optional<ExternalCredential> getExternalCredential(Long id) {
        return externalCredentialRepository.findById(id);
    }

    @Override
    public void createExternalCredential(CreateExternalCredentialCmd createExternalCredentialCmd) {
        final ExternalCredential credential = new ExternalCredential(
                createExternalCredentialCmd.getName(),
                createExternalCredentialCmd.getVersion(),
                createExternalCredentialCmd.getCredentialDefinitionId(),
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
    public void deleteExternalCredential(Long id) {
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
    public void updateExternalCredential(Long id, CreateExternalCredentialCmd createExternalCredentialCmd) {
        final Optional<ExternalCredential> optionalCredential = externalCredentialRepository.findById(id);

        if (optionalCredential.isEmpty()) {
            return;
        }

        final ExternalCredential credential = optionalCredential.get();

        credential.setName(createExternalCredentialCmd.getName());
        credential.setCredentialDefinitionId(createExternalCredentialCmd.getCredentialDefinitionId());

        externalCredentialRepository.save(credential);
    }

    /**
     * Ruft externe Credentials anhand der angegebenen Credential-Definition-ID ab.
     *
     * @param id Die Credential-Definition-ID.
     * @return Eine Liste der gefundenen externen Credentials.
     */
    @Override
    public List<ExternalCredential> getExternalCredentialByCredentialDefinitionId(String id) {
        return externalCredentialRepository.findAllByCredentialDefinitionId(id);
    }

    /**
     * Ruft alle externen Credentials als ExternalCredentialCmd-Objekte ab.
     *
     * @return Eine Liste von ExternalCredentialCmd-Objekten, die alle externen Credentials repräsentieren.
     */
    @Override
    public List<ExternalCredentialCmd> getAllExternal() {
        List<ExternalCredential> externalCredentials = getExternalCredentials();
        List<ExternalCredentialCmd> cmds = new ArrayList<>();
        List<Category> categories = categoryService.getCategory();
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
            cmds.add(new ExternalCredentialCmd(categoryName, name, intern));

        }
        return cmds;
    }

    /**
     * Ruft alle externen Credentials anhand der Proof-Configs an einem Standort ab
     *
     * @param location Standort zum Abrufen der ProofConfigs zum Erhalten der externen Credentials
     */

    private Iterable<ExternalCredential> getCredentialFromAttachedProofConfig(Location location) {
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
                .flatMap(definitionId -> getExternalCredentialByCredentialDefinitionId(definitionId)
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
    public List<ExternalCredentialCmd> getAllExternalByLocation(Long id) {
        List<Category> categories = categoryService.getCategory();
        final Location location = locationService.getLocation(id)
                .orElseThrow(() -> new IllegalArgumentException("Location with id " + id + " does not exist"));

        final Iterable<ExternalCredential> credentials = getCredentialFromAttachedProofConfig(location);

        final List<ExternalCredentialCmd> cmds = new ArrayList<>();

        for (final ExternalCredential credential : credentials) {

            final String credentialName = credential.getName();
            final List<String> internalCredentials = new ArrayList<>();
            String categoryName = "";
            for (Category category : categories) {
                if (category.getExternalCredentials().contains(credential)) {
                    categoryName = category.getName();
                    for (InternalCredential internal : category.getCredentials()) {
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
    public void saveAll(Iterable<ExternalCredential> externalCredentials) {
        externalCredentialRepository.saveAll(externalCredentials);
    }
}
