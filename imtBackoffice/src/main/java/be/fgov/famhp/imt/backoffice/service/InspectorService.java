package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Inspector;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Inspector}.
 */
public interface InspectorService {
    /**
     * Save a inspector.
     *
     * @param inspector the entity to save.
     * @return the persisted entity.
     */
    Inspector save(Inspector inspector);

    /**
     * Updates a inspector.
     *
     * @param inspector the entity to update.
     * @return the persisted entity.
     */
    Inspector update(Inspector inspector);

    /**
     * Partially updates a inspector.
     *
     * @param inspector the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inspector> partialUpdate(Inspector inspector);

    /**
     * Get all the inspectors.
     *
     * @return the list of entities.
     */
    List<Inspector> findAll();

    /**
     * Get the "id" inspector.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inspector> findOne(String id);

    /**
     * Delete the "id" inspector.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
