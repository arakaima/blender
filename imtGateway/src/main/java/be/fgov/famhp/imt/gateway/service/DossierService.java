package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.DossierDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Dossier}.
 */
public interface DossierService {
    /**
     * Save a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DossierDTO> save(DossierDTO dossierDTO);

    /**
     * Updates a dossier.
     *
     * @param dossierDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DossierDTO> update(DossierDTO dossierDTO);

    /**
     * Partially updates a dossier.
     *
     * @param dossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DossierDTO> partialUpdate(DossierDTO dossierDTO);

    /**
     * Get all the dossiers.
     *
     * @return the list of entities.
     */
    Flux<DossierDTO> findAll();

    /**
     * Returns the number of dossiers available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" dossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DossierDTO> findOne(String id);

    /**
     * Delete the "id" dossier.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
