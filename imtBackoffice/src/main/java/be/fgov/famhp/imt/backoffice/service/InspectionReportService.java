package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InspectionReport}.
 */
public interface InspectionReportService {
    /**
     * Save a inspectionReport.
     *
     * @param inspectionReport the entity to save.
     * @return the persisted entity.
     */
    InspectionReport save(InspectionReport inspectionReport);

    /**
     * Updates a inspectionReport.
     *
     * @param inspectionReport the entity to update.
     * @return the persisted entity.
     */
    InspectionReport update(InspectionReport inspectionReport);

    /**
     * Partially updates a inspectionReport.
     *
     * @param inspectionReport the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectionReport> partialUpdate(InspectionReport inspectionReport);

    /**
     * Get all the inspectionReports.
     *
     * @return the list of entities.
     */
    List<InspectionReport> findAll();

    /**
     * Get the "id" inspectionReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectionReport> findOne(String id);

    /**
     * Delete the "id" inspectionReport.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
