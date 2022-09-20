package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import be.fgov.famhp.imt.backoffice.repository.InspectionReportRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionReportService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InspectionReport}.
 */
@Service
public class InspectionReportServiceImpl implements InspectionReportService {

    private final Logger log = LoggerFactory.getLogger(InspectionReportServiceImpl.class);

    private final InspectionReportRepository inspectionReportRepository;

    public InspectionReportServiceImpl(InspectionReportRepository inspectionReportRepository) {
        this.inspectionReportRepository = inspectionReportRepository;
    }

    @Override
    public InspectionReport save(InspectionReport inspectionReport) {
        log.debug("Request to save InspectionReport : {}", inspectionReport);
        return inspectionReportRepository.save(inspectionReport);
    }

    @Override
    public InspectionReport update(InspectionReport inspectionReport) {
        log.debug("Request to update InspectionReport : {}", inspectionReport);
        // no save call needed as we have no fields that can be updated
        return inspectionReport;
    }

    @Override
    public Optional<InspectionReport> partialUpdate(InspectionReport inspectionReport) {
        log.debug("Request to partially update InspectionReport : {}", inspectionReport);

        return inspectionReportRepository
            .findById(inspectionReport.getId())
            .map(existingInspectionReport -> {
                return existingInspectionReport;
            })// .map(inspectionReportRepository::save)
        ;
    }

    @Override
    public List<InspectionReport> findAll() {
        log.debug("Request to get all InspectionReports");
        return inspectionReportRepository.findAll();
    }

    @Override
    public Optional<InspectionReport> findOne(String id) {
        log.debug("Request to get InspectionReport : {}", id);
        return inspectionReportRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectionReport : {}", id);
        inspectionReportRepository.deleteById(id);
    }
}
