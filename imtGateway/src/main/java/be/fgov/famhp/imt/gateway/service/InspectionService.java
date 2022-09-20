package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.InspectionDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Inspection}.
 */
public interface InspectionService {
    /**
     * Save a inspection.
     *
     * @param inspectionDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InspectionDTO> save(InspectionDTO inspectionDTO);

    /**
     * Updates a inspection.
     *
     * @param inspectionDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InspectionDTO> update(InspectionDTO inspectionDTO);

    /**
     * Partially updates a inspection.
     *
     * @param inspectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InspectionDTO> partialUpdate(InspectionDTO inspectionDTO);

    /**
     * Get all the inspections.
     *
     * @return the list of entities.
     */
    Flux<InspectionDTO> findAll();

    /**
     * Returns the number of inspections available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inspection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InspectionDTO> findOne(String id);

    /**
     * Delete the "id" inspection.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
