package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import be.fgov.famhp.imt.backoffice.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorDossierService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InspectorDossier}.
 */
@Service
public class InspectorDossierServiceImpl implements InspectorDossierService {

    private final Logger log = LoggerFactory.getLogger(InspectorDossierServiceImpl.class);

    private final InspectorDossierRepository inspectorDossierRepository;

    public InspectorDossierServiceImpl(InspectorDossierRepository inspectorDossierRepository) {
        this.inspectorDossierRepository = inspectorDossierRepository;
    }

    @Override
    public InspectorDossier save(InspectorDossier inspectorDossier) {
        log.debug("Request to save InspectorDossier : {}", inspectorDossier);
        return inspectorDossierRepository.save(inspectorDossier);
    }

    @Override
    public InspectorDossier update(InspectorDossier inspectorDossier) {
        log.debug("Request to update InspectorDossier : {}", inspectorDossier);
        return inspectorDossierRepository.save(inspectorDossier);
    }

    @Override
    public Optional<InspectorDossier> partialUpdate(InspectorDossier inspectorDossier) {
        log.debug("Request to partially update InspectorDossier : {}", inspectorDossier);

        return inspectorDossierRepository
            .findById(inspectorDossier.getId())
            .map(existingInspectorDossier -> {
                if (inspectorDossier.getInspectorId() != null) {
                    existingInspectorDossier.setInspectorId(inspectorDossier.getInspectorId());
                }
                if (inspectorDossier.getDossierId() != null) {
                    existingInspectorDossier.setDossierId(inspectorDossier.getDossierId());
                }
                if (inspectorDossier.getInspectorRole() != null) {
                    existingInspectorDossier.setInspectorRole(inspectorDossier.getInspectorRole());
                }
                if (inspectorDossier.getExpertId() != null) {
                    existingInspectorDossier.setExpertId(inspectorDossier.getExpertId());
                }
                if (inspectorDossier.getNumberOfDays() != null) {
                    existingInspectorDossier.setNumberOfDays(inspectorDossier.getNumberOfDays());
                }
                if (inspectorDossier.getInspectorEmployer() != null) {
                    existingInspectorDossier.setInspectorEmployer(inspectorDossier.getInspectorEmployer());
                }

                return existingInspectorDossier;
            })
            .map(inspectorDossierRepository::save);
    }

    @Override
    public List<InspectorDossier> findAll() {
        log.debug("Request to get all InspectorDossiers");
        return inspectorDossierRepository.findAll();
    }

    @Override
    public Optional<InspectorDossier> findOne(String id) {
        log.debug("Request to get InspectorDossier : {}", id);
        return inspectorDossierRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectorDossier : {}", id);
        inspectorDossierRepository.deleteById(id);
    }
}
