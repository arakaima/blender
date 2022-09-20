package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.InspectionReportDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectionReport}.
 */
public interface InspectionReportService {
    /**
     * Save a inspectionReport.
     *
     * @param inspectionReportDTO the entity to save.
     * @return the persisted entity.
     */
    InspectionReportDTO save(InspectionReportDTO inspectionReportDTO);

    /**
     * Updates a inspectionReport.
     *
     * @param inspectionReportDTO the entity to update.
     * @return the persisted entity.
     */
    InspectionReportDTO update(InspectionReportDTO inspectionReportDTO);

    /**
     * Partially updates a inspectionReport.
     *
     * @param inspectionReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectionReportDTO> partialUpdate(InspectionReportDTO inspectionReportDTO);

    /**
     * Get all the inspectionReports.
     *
     * @return the list of entities.
     */
    List<InspectionReportDTO> findAll();

    /**
     * Get the "id" inspectionReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectionReportDTO> findOne(String id);

    /**
     * Delete the "id" inspectionReport.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
