package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.InspectionTypeDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.InspectionType}.
 */
public interface InspectionTypeService {
    /**
     * Save a inspectionType.
     *
     * @param inspectionTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InspectionTypeDTO> save(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Updates a inspectionType.
     *
     * @param inspectionTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InspectionTypeDTO> update(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Partially updates a inspectionType.
     *
     * @param inspectionTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InspectionTypeDTO> partialUpdate(InspectionTypeDTO inspectionTypeDTO);

    /**
     * Get all the inspectionTypes.
     *
     * @return the list of entities.
     */
    Flux<InspectionTypeDTO> findAll();

    /**
     * Returns the number of inspectionTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inspectionType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InspectionTypeDTO> findOne(String id);

    /**
     * Delete the "id" inspectionType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
