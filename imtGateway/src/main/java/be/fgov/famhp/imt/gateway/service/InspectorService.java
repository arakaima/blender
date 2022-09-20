package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.InspectorDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Inspector}.
 */
public interface InspectorService {
    /**
     * Save a inspector.
     *
     * @param inspectorDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InspectorDTO> save(InspectorDTO inspectorDTO);

    /**
     * Updates a inspector.
     *
     * @param inspectorDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InspectorDTO> update(InspectorDTO inspectorDTO);

    /**
     * Partially updates a inspector.
     *
     * @param inspectorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InspectorDTO> partialUpdate(InspectorDTO inspectorDTO);

    /**
     * Get all the inspectors.
     *
     * @return the list of entities.
     */
    Flux<InspectorDTO> findAll();

    /**
     * Returns the number of inspectors available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inspector.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InspectorDTO> findOne(String id);

    /**
     * Delete the "id" inspector.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
