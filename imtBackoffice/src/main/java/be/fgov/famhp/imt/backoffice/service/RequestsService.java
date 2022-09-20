package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Requests}.
 */
public interface RequestsService {
    /**
     * Save a requests.
     *
     * @param requests the entity to save.
     * @return the persisted entity.
     */
    Requests save(Requests requests);

    /**
     * Updates a requests.
     *
     * @param requests the entity to update.
     * @return the persisted entity.
     */
    Requests update(Requests requests);

    /**
     * Partially updates a requests.
     *
     * @param requests the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Requests> partialUpdate(Requests requests);

    /**
     * Get all the requests.
     *
     * @return the list of entities.
     */
    List<Requests> findAll();

    /**
     * Get the "id" requests.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Requests> findOne(String id);

    /**
     * Delete the "id" requests.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
