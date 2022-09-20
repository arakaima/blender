package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Expert;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Expert}.
 */
public interface ExpertService {
    /**
     * Save a expert.
     *
     * @param expert the entity to save.
     * @return the persisted entity.
     */
    Expert save(Expert expert);

    /**
     * Updates a expert.
     *
     * @param expert the entity to update.
     * @return the persisted entity.
     */
    Expert update(Expert expert);

    /**
     * Partially updates a expert.
     *
     * @param expert the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Expert> partialUpdate(Expert expert);

    /**
     * Get all the experts.
     *
     * @return the list of entities.
     */
    List<Expert> findAll();

    /**
     * Get the "id" expert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Expert> findOne(String id);

    /**
     * Delete the "id" expert.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
