package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.ExpertDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Expert}.
 */
public interface ExpertService {
    /**
     * Save a expert.
     *
     * @param expertDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ExpertDTO> save(ExpertDTO expertDTO);

    /**
     * Updates a expert.
     *
     * @param expertDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ExpertDTO> update(ExpertDTO expertDTO);

    /**
     * Partially updates a expert.
     *
     * @param expertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ExpertDTO> partialUpdate(ExpertDTO expertDTO);

    /**
     * Get all the experts.
     *
     * @return the list of entities.
     */
    Flux<ExpertDTO> findAll();

    /**
     * Returns the number of experts available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" expert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ExpertDTO> findOne(String id);

    /**
     * Delete the "id" expert.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
