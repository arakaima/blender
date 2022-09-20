package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.RequestsDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Requests}.
 */
public interface RequestsService {
    /**
     * Save a requests.
     *
     * @param requestsDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RequestsDTO> save(RequestsDTO requestsDTO);

    /**
     * Updates a requests.
     *
     * @param requestsDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RequestsDTO> update(RequestsDTO requestsDTO);

    /**
     * Partially updates a requests.
     *
     * @param requestsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RequestsDTO> partialUpdate(RequestsDTO requestsDTO);

    /**
     * Get all the requests.
     *
     * @return the list of entities.
     */
    Flux<RequestsDTO> findAll();

    /**
     * Returns the number of requests available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" requests.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RequestsDTO> findOne(String id);

    /**
     * Delete the "id" requests.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
