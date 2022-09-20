package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.AttachedDocumentDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.AttachedDocument}.
 */
public interface AttachedDocumentService {
    /**
     * Save a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AttachedDocumentDTO> save(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Updates a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AttachedDocumentDTO> update(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Partially updates a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AttachedDocumentDTO> partialUpdate(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Get all the attachedDocuments.
     *
     * @return the list of entities.
     */
    Flux<AttachedDocumentDTO> findAll();

    /**
     * Returns the number of attachedDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" attachedDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AttachedDocumentDTO> findOne(String id);

    /**
     * Delete the "id" attachedDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
