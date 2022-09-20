package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AttachedDocument}.
 */
public interface AttachedDocumentService {
    /**
     * Save a attachedDocument.
     *
     * @param attachedDocument the entity to save.
     * @return the persisted entity.
     */
    AttachedDocument save(AttachedDocument attachedDocument);

    /**
     * Updates a attachedDocument.
     *
     * @param attachedDocument the entity to update.
     * @return the persisted entity.
     */
    AttachedDocument update(AttachedDocument attachedDocument);

    /**
     * Partially updates a attachedDocument.
     *
     * @param attachedDocument the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AttachedDocument> partialUpdate(AttachedDocument attachedDocument);

    /**
     * Get all the attachedDocuments.
     *
     * @return the list of entities.
     */
    List<AttachedDocument> findAll();

    /**
     * Get the "id" attachedDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AttachedDocument> findOne(String id);

    /**
     * Delete the "id" attachedDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
