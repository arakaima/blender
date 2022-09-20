package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import be.fgov.famhp.imt.backoffice.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionTypeService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InspectionType}.
 */
@Service
public class InspectionTypeServiceImpl implements InspectionTypeService {

    private final Logger log = LoggerFactory.getLogger(InspectionTypeServiceImpl.class);

    private final InspectionTypeRepository inspectionTypeRepository;

    public InspectionTypeServiceImpl(InspectionTypeRepository inspectionTypeRepository) {
        this.inspectionTypeRepository = inspectionTypeRepository;
    }

    @Override
    public InspectionType save(InspectionType inspectionType) {
        log.debug("Request to save InspectionType : {}", inspectionType);
        return inspectionTypeRepository.save(inspectionType);
    }

    @Override
    public InspectionType update(InspectionType inspectionType) {
        log.debug("Request to update InspectionType : {}", inspectionType);
        return inspectionTypeRepository.save(inspectionType);
    }

    @Override
    public Optional<InspectionType> partialUpdate(InspectionType inspectionType) {
        log.debug("Request to partially update InspectionType : {}", inspectionType);

        return inspectionTypeRepository
            .findById(inspectionType.getId())
            .map(existingInspectionType -> {
                if (inspectionType.getNameFr() != null) {
                    existingInspectionType.setNameFr(inspectionType.getNameFr());
                }
                if (inspectionType.getNameEn() != null) {
                    existingInspectionType.setNameEn(inspectionType.getNameEn());
                }
                if (inspectionType.getNameNl() != null) {
                    existingInspectionType.setNameNl(inspectionType.getNameNl());
                }
                if (inspectionType.getNameDe() != null) {
                    existingInspectionType.setNameDe(inspectionType.getNameDe());
                }
                if (inspectionType.getDeprecated() != null) {
                    existingInspectionType.setDeprecated(inspectionType.getDeprecated());
                }

                return existingInspectionType;
            })
            .map(inspectionTypeRepository::save);
    }

    @Override
    public List<InspectionType> findAll() {
        log.debug("Request to get all InspectionTypes");
        return inspectionTypeRepository.findAll();
    }

    @Override
    public Optional<InspectionType> findOne(String id) {
        log.debug("Request to get InspectionType : {}", id);
        return inspectionTypeRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectionType : {}", id);
        inspectionTypeRepository.deleteById(id);
    }
}
