package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.OrganizationDocumentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.OrganizationDocument}.
 */
public interface OrganizationDocumentService {
    /**
     * Save a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    OrganizationDocumentDTO save(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Updates a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    OrganizationDocumentDTO update(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Partially updates a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganizationDocumentDTO> partialUpdate(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Get all the organizationDocuments.
     *
     * @return the list of entities.
     */
    List<OrganizationDocumentDTO> findAll();

    /**
     * Get the "id" organizationDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDocumentDTO> findOne(String id);

    /**
     * Delete the "id" organizationDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
