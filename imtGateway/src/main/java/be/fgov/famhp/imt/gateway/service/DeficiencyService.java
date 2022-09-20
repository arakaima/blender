package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.DeficiencyDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Deficiency}.
 */
public interface DeficiencyService {
    /**
     * Save a deficiency.
     *
     * @param deficiencyDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DeficiencyDTO> save(DeficiencyDTO deficiencyDTO);

    /**
     * Updates a deficiency.
     *
     * @param deficiencyDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DeficiencyDTO> update(DeficiencyDTO deficiencyDTO);

    /**
     * Partially updates a deficiency.
     *
     * @param deficiencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DeficiencyDTO> partialUpdate(DeficiencyDTO deficiencyDTO);

    /**
     * Get all the deficiencies.
     *
     * @return the list of entities.
     */
    Flux<DeficiencyDTO> findAll();

    /**
     * Returns the number of deficiencies available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" deficiency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DeficiencyDTO> findOne(String id);

    /**
     * Delete the "id" deficiency.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
