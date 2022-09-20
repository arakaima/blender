package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link RequestsMessage}.
 */
public interface RequestsMessageService {
    /**
     * Save a requestsMessage.
     *
     * @param requestsMessage the entity to save.
     * @return the persisted entity.
     */
    RequestsMessage save(RequestsMessage requestsMessage);

    /**
     * Updates a requestsMessage.
     *
     * @param requestsMessage the entity to update.
     * @return the persisted entity.
     */
    RequestsMessage update(RequestsMessage requestsMessage);

    /**
     * Partially updates a requestsMessage.
     *
     * @param requestsMessage the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RequestsMessage> partialUpdate(RequestsMessage requestsMessage);

    /**
     * Get all the requestsMessages.
     *
     * @return the list of entities.
     */
    List<RequestsMessage> findAll();

    /**
     * Get the "id" requestsMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestsMessage> findOne(String id);

    /**
     * Delete the "id" requestsMessage.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
