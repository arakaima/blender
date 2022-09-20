package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link OrganizationDocument}.
 */
public interface OrganizationDocumentService {
    /**
     * Save a organizationDocument.
     *
     * @param organizationDocument the entity to save.
     * @return the persisted entity.
     */
    OrganizationDocument save(OrganizationDocument organizationDocument);

    /**
     * Updates a organizationDocument.
     *
     * @param organizationDocument the entity to update.
     * @return the persisted entity.
     */
    OrganizationDocument update(OrganizationDocument organizationDocument);

    /**
     * Partially updates a organizationDocument.
     *
     * @param organizationDocument the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrganizationDocument> partialUpdate(OrganizationDocument organizationDocument);

    /**
     * Get all the organizationDocuments.
     *
     * @return the list of entities.
     */
    List<OrganizationDocument> findAll();

    /**
     * Get the "id" organizationDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrganizationDocument> findOne(String id);

    /**
     * Delete the "id" organizationDocument.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
