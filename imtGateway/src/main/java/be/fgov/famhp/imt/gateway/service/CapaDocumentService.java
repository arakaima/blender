package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.CapaDocumentDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.CapaDocument}.
 */
public interface CapaDocumentService {
    /**
     * Save a capaDocument.
     *
     * @param capaDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CapaDocumentDTO> save(CapaDocumentDTO capaDocumentDTO);

    /**
     * Updates a capaDocument.
     *
     * @param capaDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CapaDocumentDTO> update(CapaDocumentDTO capaDocumentDTO);

    /**
     * Partially updates a capaDocument.
     *
     * @param capaDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CapaDocumentDTO> partialUpdate(CapaDocumentDTO capaDocumentDTO);

    /**
     * Get all the capaDocuments.
     *
     * @return the list of entities.
     */
    Flux<CapaDocumentDTO> findAll();

    /**
     * Returns the number of capaDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" capaDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CapaDocumentDTO> findOne(String id);

    /**
     * Delete the "id" capaDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
