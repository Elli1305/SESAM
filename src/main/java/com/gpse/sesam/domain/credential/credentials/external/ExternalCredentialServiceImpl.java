package com.gpse.sesam.domain.credential.credentials.external;

import com.gpse.sesam.domain.credential.category.Category;
import com.gpse.sesam.domain.credential.category.CategoryService;
import com.gpse.sesam.domain.credential.credentials.internal.InternalCredential;
import com.gpse.sesam.domain.location.Location;
import com.gpse.sesam.domain.location.LocationService;
import com.gpse.sesam.domain.location.door.config.AttributeFilter;
import com.gpse.sesam.web.cmd.ExternalCredentialCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExternalCredentialServiceImpl implements ExternalCredentialService {
    private final ExternalCredentialRepository externalCredentialRepository;

    private final LocationService locationService;

    private final CategoryService categoryService;

    @Autowired
    public ExternalCredentialServiceImpl(ExternalCredentialRepository externalCredentialRepository,
                                         LocationService locationService, CategoryService categoryService) {
        this.externalCredentialRepository = externalCredentialRepository;
        this.locationService = locationService;
        this.categoryService = categoryService;
    }
    @SuppressWarnings("CPD-START")
    @Override
    public List<ExternalCredential> getExternalCredentials() {
        List<ExternalCredential> externalCredentials = new ArrayList<>();
        externalCredentialRepository.findAll().forEach(externalCredentials::add);
        return externalCredentials;
    }

    @Override
    public Optional<ExternalCredential> getExternalCredential(Long id) {
        return externalCredentialRepository.findById(id);
    }

    @Override
    public List<ExternalCredential> getExternalCredentialByCredentialDefinitionId(String id) {
        return externalCredentialRepository.findAllByCredentialDefinitionId(id);
    }

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

    private Iterable<ExternalCredential> getCredentialFromAttachedProofConfig(Location location) {
        return location
                .getBuildings().stream()
                .flatMap(building -> building.getFloors().stream())
                .flatMap(floor -> floor.getRooms().stream())
                .flatMap(room -> room.getDoors().stream())
                .flatMap(door -> door.getDoorConfigs().stream())
                .flatMap(twoWayDoorConfig -> Stream.of(twoWayDoorConfig.getProofConfigIn(), twoWayDoorConfig.getProofConfigOut()))
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

    @Override
    public void deleteAll() {
        externalCredentialRepository.deleteAll();
    }

    @Override
    public void saveAll(Iterable<ExternalCredential> externalCredentials) {
        externalCredentialRepository.saveAll(externalCredentials);
    }
}
