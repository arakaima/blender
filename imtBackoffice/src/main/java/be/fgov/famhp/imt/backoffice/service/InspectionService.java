package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.InspectionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Inspection}.
 */
public interface InspectionService {
    /**
     * Save a inspection.
     *
     * @param inspectionDTO the entity to save.
     * @return the persisted entity.
     */
    InspectionDTO save(InspectionDTO inspectionDTO);

    /**
     * Updates a inspection.
     *
     * @param inspectionDTO the entity to update.
     * @return the persisted entity.
     */
    InspectionDTO update(InspectionDTO inspectionDTO);

    /**
     * Partially updates a inspection.
     *
     * @param inspectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectionDTO> partialUpdate(InspectionDTO inspectionDTO);

    /**
     * Get all the inspections.
     *
     * @return the list of entities.
     */
    List<InspectionDTO> findAll();

    /**
     * Get the "id" inspection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectionDTO> findOne(String id);

    /**
     * Delete the "id" inspection.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
