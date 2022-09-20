package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.RequestsMessageDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.RequestsMessage}.
 */
public interface RequestsMessageService {
    /**
     * Save a requestsMessage.
     *
     * @param requestsMessageDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RequestsMessageDTO> save(RequestsMessageDTO requestsMessageDTO);

    /**
     * Updates a requestsMessage.
     *
     * @param requestsMessageDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RequestsMessageDTO> update(RequestsMessageDTO requestsMessageDTO);

    /**
     * Partially updates a requestsMessage.
     *
     * @param requestsMessageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RequestsMessageDTO> partialUpdate(RequestsMessageDTO requestsMessageDTO);

    /**
     * Get all the requestsMessages.
     *
     * @return the list of entities.
     */
    Flux<RequestsMessageDTO> findAll();

    /**
     * Returns the number of requestsMessages available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" requestsMessage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RequestsMessageDTO> findOne(String id);

    /**
     * Delete the "id" requestsMessage.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
