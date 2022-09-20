package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.SiteDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Site}.
 */
public interface SiteService {
    /**
     * Save a site.
     *
     * @param siteDTO the entity to save.
     * @return the persisted entity.
     */
    SiteDTO save(SiteDTO siteDTO);

    /**
     * Updates a site.
     *
     * @param siteDTO the entity to update.
     * @return the persisted entity.
     */
    SiteDTO update(SiteDTO siteDTO);

    /**
     * Partially updates a site.
     *
     * @param siteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SiteDTO> partialUpdate(SiteDTO siteDTO);

    /**
     * Get all the sites.
     *
     * @return the list of entities.
     */
    List<SiteDTO> findAll();

    /**
     * Get the "id" site.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SiteDTO> findOne(String id);

    /**
     * Delete the "id" site.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
