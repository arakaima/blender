package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Deficiency;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Deficiency}.
 */
public interface DeficiencyService {
    /**
     * Save a deficiency.
     *
     * @param deficiency the entity to save.
     * @return the persisted entity.
     */
    Deficiency save(Deficiency deficiency);

    /**
     * Updates a deficiency.
     *
     * @param deficiency the entity to update.
     * @return the persisted entity.
     */
    Deficiency update(Deficiency deficiency);

    /**
     * Partially updates a deficiency.
     *
     * @param deficiency the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Deficiency> partialUpdate(Deficiency deficiency);

    /**
     * Get all the deficiencies.
     *
     * @return the list of entities.
     */
    List<Deficiency> findAll();

    /**
     * Get the "id" deficiency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Deficiency> findOne(String id);

    /**
     * Delete the "id" deficiency.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
