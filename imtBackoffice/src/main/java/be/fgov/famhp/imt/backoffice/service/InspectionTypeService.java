package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.InspectionTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectionType}.
 */
public interface InspectionTypeService {
    /**
     * Save a inspectionType.
     *
     * @param inspectionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    InspectionTypeDTO save(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Updates a inspectionType.
     *
     * @param inspectionTypeDTO the entity to update.
     * @return the persisted entity.
     */
    InspectionTypeDTO update(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Partially updates a inspectionType.
     *
     * @param inspectionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectionTypeDTO> partialUpdate(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Get all the inspectionTypes.
     *
     * @return the list of entities.
     */
    List<InspectionTypeDTO> findAll();

    /**
     * Get the "id" inspectionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectionTypeDTO> findOne(String id);

    /**
     * Delete the "id" inspectionType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
