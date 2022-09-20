package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.InspectorDossierDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.InspectorDossier}.
 */
public interface InspectorDossierService {
    /**
     * Save a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<InspectorDossierDTO> save(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Updates a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<InspectorDossierDTO> update(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Partially updates a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<InspectorDossierDTO> partialUpdate(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Get all the inspectorDossiers.
     *
     * @return the list of entities.
     */
    Flux<InspectorDossierDTO> findAll();

    /**
     * Returns the number of inspectorDossiers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<InspectorDossierDTO> findOne(String id);

    /**
     * Delete the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
