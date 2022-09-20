package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.DossierStatusDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.DossierStatus}.
 */
public interface DossierStatusService {
    /**
     * Save a dossierStatus.
     *
     * @param dossierStatusDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DossierStatusDTO> save(DossierStatusDTO dossierStatusDTO);

    /**
     * Updates a dossierStatus.
     *
     * @param dossierStatusDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DossierStatusDTO> update(DossierStatusDTO dossierStatusDTO);

    /**
     * Partially updates a dossierStatus.
     *
     * @param dossierStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DossierStatusDTO> partialUpdate(DossierStatusDTO dossierStatusDTO);

    /**
     * Get all the dossierStatuses.
     *
     * @return the list of entities.
     */
    Flux<DossierStatusDTO> findAll();

    /**
     * Returns the number of dossierStatuses available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" dossierStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DossierStatusDTO> findOne(String id);

    /**
     * Delete the "id" dossierStatus.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
