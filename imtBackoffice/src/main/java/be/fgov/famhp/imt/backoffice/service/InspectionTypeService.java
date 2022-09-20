package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InspectionType}.
 */
public interface InspectionTypeService {
    /**
     * Save a inspectionType.
     *
     * @param inspectionType the entity to save.
     * @return the persisted entity.
     */
    InspectionType save(InspectionType inspectionType);

    /**
     * Updates a inspectionType.
     *
     * @param inspectionType the entity to update.
     * @return the persisted entity.
     */
    InspectionType update(InspectionType inspectionType);

    /**
     * Partially updates a inspectionType.
     *
     * @param inspectionType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectionType> partialUpdate(InspectionType inspectionType);

    /**
     * Get all the inspectionTypes.
     *
     * @return the list of entities.
     */
    List<InspectionType> findAll();

    /**
     * Get the "id" inspectionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectionType> findOne(String id);

    /**
     * Delete the "id" inspectionType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
