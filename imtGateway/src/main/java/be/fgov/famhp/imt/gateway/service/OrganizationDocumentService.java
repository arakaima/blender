package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.OrganizationDocumentDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.OrganizationDocument}.
 */
public interface OrganizationDocumentService {
    /**
     * Save a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<OrganizationDocumentDTO> save(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Updates a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<OrganizationDocumentDTO> update(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Partially updates a organizationDocument.
     *
     * @param organizationDocumentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<OrganizationDocumentDTO> partialUpdate(OrganizationDocumentDTO organizationDocumentDTO);

    /**
     * Get all the organizationDocuments.
     *
     * @return the list of entities.
     */
    Flux<OrganizationDocumentDTO> findAll();

    /**
     * Returns the number of organizationDocuments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" organizationDocument.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<OrganizationDocumentDTO> findOne(String id);

    /**
     * Delete the "id" organizationDocument.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
