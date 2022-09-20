package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.OrganizationDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Organization}.
 */
public interface OrganizationService {
    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<OrganizationDTO> save(OrganizationDTO organizationDTO);

    /**
     * Updates a organization.
     *
     * @param organizationDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<OrganizationDTO> update(OrganizationDTO organizationDTO);

    /**
     * Partially updates a organization.
     *
     * @param organizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<OrganizationDTO> partialUpdate(OrganizationDTO organizationDTO);

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    Flux<OrganizationDTO> findAll();

    /**
     * Returns the number of organizations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" organization.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<OrganizationDTO> findOne(String id);

    /**
     * Delete the "id" organization.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
