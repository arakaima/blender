package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.RequestsMessageDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.RequestsMessage}.
 */
public interface RequestsMessageService {
    /**
     * Save a requestsMessage.
     *
     * @param requestsMessageDTO the entity to save.
     * @return the persisted entity.
     */
    RequestsMessageDTO save(RequestsMessageDTO requestsMessageDTO);

    /**
     * Updates a requestsMessage.
     *
     * @param requestsMessageDTO the entity to update.
     * @return the persisted entity.
     */
    RequestsMessageDTO update(RequestsMessageDTO requestsMessageDTO);

    /**
     * Partially updates a requestsMessage.
     *
     * @param requestsMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RequestsMessageDTO> partialUpdate(RequestsMessageDTO requestsMessageDTO);

    /**
     * Get all the requestsMessages.
     *
     * @return the list of entities.
     */
    List<RequestsMessageDTO> findAll();

    /**
     * Get the "id" requestsMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestsMessageDTO> findOne(String id);

    /**
     * Delete the "id" requestsMessage.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
