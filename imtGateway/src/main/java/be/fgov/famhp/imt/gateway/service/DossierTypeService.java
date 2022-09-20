package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.DossierTypeDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.DossierType}.
 */
public interface DossierTypeService {
    /**
     * Save a dossierType.
     *
     * @param dossierTypeDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<DossierTypeDTO> save(DossierTypeDTO dossierTypeDTO);

    /**
     * Updates a dossierType.
     *
     * @param dossierTypeDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<DossierTypeDTO> update(DossierTypeDTO dossierTypeDTO);

    /**
     * Partially updates a dossierType.
     *
     * @param dossierTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<DossierTypeDTO> partialUpdate(DossierTypeDTO dossierTypeDTO);

    /**
     * Get all the dossierTypes.
     *
     * @return the list of entities.
     */
    Flux<DossierTypeDTO> findAll();

    /**
     * Returns the number of dossierTypes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" dossierType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<DossierTypeDTO> findOne(String id);

    /**
     * Delete the "id" dossierType.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
