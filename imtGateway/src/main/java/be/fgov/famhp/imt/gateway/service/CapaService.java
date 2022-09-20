package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.CapaDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Capa}.
 */
public interface CapaService {
    /**
     * Save a capa.
     *
     * @param capaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CapaDTO> save(CapaDTO capaDTO);

    /**
     * Updates a capa.
     *
     * @param capaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CapaDTO> update(CapaDTO capaDTO);

    /**
     * Partially updates a capa.
     *
     * @param capaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CapaDTO> partialUpdate(CapaDTO capaDTO);

    /**
     * Get all the capas.
     *
     * @return the list of entities.
     */
    Flux<CapaDTO> findAll();

    /**
     * Returns the number of capas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" capa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CapaDTO> findOne(String id);

    /**
     * Delete the "id" capa.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
