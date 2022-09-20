package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.AttachedDocumentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.AttachedDocument}.
 */
public interface AttachedDocumentService {
    /**
     * Save a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    AttachedDocumentDTO save(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Updates a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    AttachedDocumentDTO update(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Partially updates a attachedDocument.
     *
     * @param attachedDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttachedDocumentDTO> partialUpdate(AttachedDocumentDTO attachedDocumentDTO);

    /**
     * Get all the attachedDocuments.
     *
     * @return the list of entities.
     */
    List<AttachedDocumentDTO> findAll();

    /**
     * Get the "id" attachedDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttachedDocumentDTO> findOne(String id);

    /**
     * Delete the "id" attachedDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
