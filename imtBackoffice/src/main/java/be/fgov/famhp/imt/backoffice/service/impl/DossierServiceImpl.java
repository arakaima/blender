package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Dossier;
import be.fgov.famhp.imt.backoffice.repository.DossierRepository;
import be.fgov.famhp.imt.backoffice.service.DossierService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
public class DossierServiceImpl implements DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierServiceImpl.class);

    private final DossierRepository dossierRepository;

    public DossierServiceImpl(DossierRepository dossierRepository) {
        this.dossierRepository = dossierRepository;
    }

    @Override
    public Dossier save(Dossier dossier) {
        log.debug("Request to save Dossier : {}", dossier);
        return dossierRepository.save(dossier);
    }

    @Override
    public Dossier update(Dossier dossier) {
        log.debug("Request to update Dossier : {}", dossier);
        return dossierRepository.save(dossier);
    }

    @Override
    public Optional<Dossier> partialUpdate(Dossier dossier) {
        log.debug("Request to partially update Dossier : {}", dossier);

        return dossierRepository
            .findById(dossier.getId())
            .map(existingDossier -> {
                if (dossier.getDossierNumber() != null) {
                    existingDossier.setDossierNumber(dossier.getDossierNumber());
                }
                if (dossier.getDescription() != null) {
                    existingDossier.setDescription(dossier.getDescription());
                }
                if (dossier.getDossierType() != null) {
                    existingDossier.setDossierType(dossier.getDossierType());
                }
                if (dossier.getDossierStatus() != null) {
                    existingDossier.setDossierStatus(dossier.getDossierStatus());
                }
                if (dossier.getInspectionEntity() != null) {
                    existingDossier.setInspectionEntity(dossier.getInspectionEntity());
                }

                return existingDossier;
            })
            .map(dossierRepository::save);
    }

    @Override
    public List<Dossier> findAll() {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll();
    }

    @Override
    public Optional<Dossier> findOne(String id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
    }
}
