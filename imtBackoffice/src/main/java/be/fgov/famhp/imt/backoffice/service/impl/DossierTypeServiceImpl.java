package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.DossierType;
import be.fgov.famhp.imt.backoffice.repository.DossierTypeRepository;
import be.fgov.famhp.imt.backoffice.service.DossierTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DossierType}.
 */
@Service
public class DossierTypeServiceImpl implements DossierTypeService {

    private final Logger log = LoggerFactory.getLogger(DossierTypeServiceImpl.class);

    private final DossierTypeRepository dossierTypeRepository;

    public DossierTypeServiceImpl(DossierTypeRepository dossierTypeRepository) {
        this.dossierTypeRepository = dossierTypeRepository;
    }

    @Override
    public DossierType save(DossierType dossierType) {
        log.debug("Request to save DossierType : {}", dossierType);
        return dossierTypeRepository.save(dossierType);
    }

    @Override
    public DossierType update(DossierType dossierType) {
        log.debug("Request to update DossierType : {}", dossierType);
        return dossierTypeRepository.save(dossierType);
    }

    @Override
    public Optional<DossierType> partialUpdate(DossierType dossierType) {
        log.debug("Request to partially update DossierType : {}", dossierType);

        return dossierTypeRepository
            .findById(dossierType.getId())
            .map(existingDossierType -> {
                if (dossierType.getNameFr() != null) {
                    existingDossierType.setNameFr(dossierType.getNameFr());
                }
                if (dossierType.getNameEn() != null) {
                    existingDossierType.setNameEn(dossierType.getNameEn());
                }
                if (dossierType.getNameNl() != null) {
                    existingDossierType.setNameNl(dossierType.getNameNl());
                }
                if (dossierType.getNameDe() != null) {
                    existingDossierType.setNameDe(dossierType.getNameDe());
                }
                if (dossierType.getDeprecated() != null) {
                    existingDossierType.setDeprecated(dossierType.getDeprecated());
                }

                return existingDossierType;
            })
            .map(dossierTypeRepository::save);
    }

    @Override
    public List<DossierType> findAll() {
        log.debug("Request to get all DossierTypes");
        return dossierTypeRepository.findAll();
    }

    @Override
    public Optional<DossierType> findOne(String id) {
        log.debug("Request to get DossierType : {}", id);
        return dossierTypeRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DossierType : {}", id);
        dossierTypeRepository.deleteById(id);
    }
}
