package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Inspection;
import be.fgov.famhp.imt.backoffice.repository.InspectionRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Inspection}.
 */
@Service
public class InspectionServiceImpl implements InspectionService {

    private final Logger log = LoggerFactory.getLogger(InspectionServiceImpl.class);

    private final InspectionRepository inspectionRepository;

    public InspectionServiceImpl(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public Inspection save(Inspection inspection) {
        log.debug("Request to save Inspection : {}", inspection);
        return inspectionRepository.save(inspection);
    }

    @Override
    public Inspection update(Inspection inspection) {
        log.debug("Request to update Inspection : {}", inspection);
        // no save call needed as we have no fields that can be updated
        return inspection;
    }

    @Override
    public Optional<Inspection> partialUpdate(Inspection inspection) {
        log.debug("Request to partially update Inspection : {}", inspection);

        return inspectionRepository
            .findById(inspection.getId())
            .map(existingInspection -> {
                return existingInspection;
            })// .map(inspectionRepository::save)
        ;
    }

    @Override
    public List<Inspection> findAll() {
        log.debug("Request to get all Inspections");
        return inspectionRepository.findAll();
    }

    @Override
    public Optional<Inspection> findOne(String id) {
        log.debug("Request to get Inspection : {}", id);
        return inspectionRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Inspection : {}", id);
        inspectionRepository.deleteById(id);
    }
}
