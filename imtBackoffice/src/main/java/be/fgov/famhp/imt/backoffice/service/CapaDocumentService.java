package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link CapaDocument}.
 */
public interface CapaDocumentService {
    /**
     * Save a capaDocument.
     *
     * @param capaDocument the entity to save.
     * @return the persisted entity.
     */
    CapaDocument save(CapaDocument capaDocument);

    /**
     * Updates a capaDocument.
     *
     * @param capaDocument the entity to update.
     * @return the persisted entity.
     */
    CapaDocument update(CapaDocument capaDocument);

    /**
     * Partially updates a capaDocument.
     *
     * @param capaDocument the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CapaDocument> partialUpdate(CapaDocument capaDocument);

    /**
     * Get all the capaDocuments.
     *
     * @return the list of entities.
     */
    List<CapaDocument> findAll();

    /**
     * Get the "id" capaDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CapaDocument> findOne(String id);

    /**
     * Delete the "id" capaDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
