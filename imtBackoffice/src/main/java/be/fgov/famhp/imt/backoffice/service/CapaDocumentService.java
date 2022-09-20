package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.CapaDocumentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.CapaDocument}.
 */
public interface CapaDocumentService {
    /**
     * Save a capaDocument.
     *
     * @param capaDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    CapaDocumentDTO save(CapaDocumentDTO capaDocumentDTO);

    /**
     * Updates a capaDocument.
     *
     * @param capaDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    CapaDocumentDTO update(CapaDocumentDTO capaDocumentDTO);

    /**
     * Partially updates a capaDocument.
     *
     * @param capaDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CapaDocumentDTO> partialUpdate(CapaDocumentDTO capaDocumentDTO);

    /**
     * Get all the capaDocuments.
     *
     * @return the list of entities.
     */
    List<CapaDocumentDTO> findAll();

    /**
     * Get the "id" capaDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CapaDocumentDTO> findOne(String id);

    /**
     * Delete the "id" capaDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
