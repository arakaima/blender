package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.InspectionReportDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.InspectionReport}.
 */
public interface InspectionReportService {
    /**
     * Save a inspectionReport.
     *
     * @param inspectionReportDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InspectionReportDTO> save(InspectionReportDTO inspectionReportDTO);

    /**
     * Updates a inspectionReport.
     *
     * @param inspectionReportDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InspectionReportDTO> update(InspectionReportDTO inspectionReportDTO);

    /**
     * Partially updates a inspectionReport.
     *
     * @param inspectionReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InspectionReportDTO> partialUpdate(InspectionReportDTO inspectionReportDTO);

    /**
     * Get all the inspectionReports.
     *
     * @return the list of entities.
     */
    Flux<InspectionReportDTO> findAll();

    /**
     * Returns the number of inspectionReports available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inspectionReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InspectionReportDTO> findOne(String id);

    /**
     * Delete the "id" inspectionReport.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
