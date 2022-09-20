package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Inspection;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Inspection}.
 */
public interface InspectionService {
    /**
     * Save a inspection.
     *
     * @param inspection the entity to save.
     * @return the persisted entity.
     */
    Inspection save(Inspection inspection);

    /**
     * Updates a inspection.
     *
     * @param inspection the entity to update.
     * @return the persisted entity.
     */
    Inspection update(Inspection inspection);

    /**
     * Partially updates a inspection.
     *
     * @param inspection the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Inspection> partialUpdate(Inspection inspection);

    /**
     * Get all the inspections.
     *
     * @return the list of entities.
     */
    List<Inspection> findAll();

    /**
     * Get the "id" inspection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Inspection> findOne(String id);

    /**
     * Delete the "id" inspection.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
