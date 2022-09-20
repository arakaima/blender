package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import be.fgov.famhp.imt.backoffice.repository.DossierStatusRepository;
import be.fgov.famhp.imt.backoffice.service.DossierStatusService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DossierStatus}.
 */
@Service
public class DossierStatusServiceImpl implements DossierStatusService {

    private final Logger log = LoggerFactory.getLogger(DossierStatusServiceImpl.class);

    private final DossierStatusRepository dossierStatusRepository;

    public DossierStatusServiceImpl(DossierStatusRepository dossierStatusRepository) {
        this.dossierStatusRepository = dossierStatusRepository;
    }

    @Override
    public DossierStatus save(DossierStatus dossierStatus) {
        log.debug("Request to save DossierStatus : {}", dossierStatus);
        return dossierStatusRepository.save(dossierStatus);
    }

    @Override
    public DossierStatus update(DossierStatus dossierStatus) {
        log.debug("Request to update DossierStatus : {}", dossierStatus);
        return dossierStatusRepository.save(dossierStatus);
    }

    @Override
    public Optional<DossierStatus> partialUpdate(DossierStatus dossierStatus) {
        log.debug("Request to partially update DossierStatus : {}", dossierStatus);

        return dossierStatusRepository
            .findById(dossierStatus.getId())
            .map(existingDossierStatus -> {
                if (dossierStatus.getNameFr() != null) {
                    existingDossierStatus.setNameFr(dossierStatus.getNameFr());
                }
                if (dossierStatus.getNameEn() != null) {
                    existingDossierStatus.setNameEn(dossierStatus.getNameEn());
                }
                if (dossierStatus.getNameNl() != null) {
                    existingDossierStatus.setNameNl(dossierStatus.getNameNl());
                }
                if (dossierStatus.getNameDe() != null) {
                    existingDossierStatus.setNameDe(dossierStatus.getNameDe());
                }
                if (dossierStatus.getDeprecated() != null) {
                    existingDossierStatus.setDeprecated(dossierStatus.getDeprecated());
                }

                return existingDossierStatus;
            })
            .map(dossierStatusRepository::save);
    }

    @Override
    public List<DossierStatus> findAll() {
        log.debug("Request to get all DossierStatuses");
        return dossierStatusRepository.findAll();
    }

    @Override
    public Optional<DossierStatus> findOne(String id) {
        log.debug("Request to get DossierStatus : {}", id);
        return dossierStatusRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DossierStatus : {}", id);
        dossierStatusRepository.deleteById(id);
    }
}
