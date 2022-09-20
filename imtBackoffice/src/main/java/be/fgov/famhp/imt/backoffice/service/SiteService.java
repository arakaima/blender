package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Site;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Site}.
 */
public interface SiteService {
    /**
     * Save a site.
     *
     * @param site the entity to save.
     * @return the persisted entity.
     */
    Site save(Site site);

    /**
     * Updates a site.
     *
     * @param site the entity to update.
     * @return the persisted entity.
     */
    Site update(Site site);

    /**
     * Partially updates a site.
     *
     * @param site the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Site> partialUpdate(Site site);

    /**
     * Get all the sites.
     *
     * @return the list of entities.
     */
    List<Site> findAll();

    /**
     * Get the "id" site.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Site> findOne(String id);

    /**
     * Delete the "id" site.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
