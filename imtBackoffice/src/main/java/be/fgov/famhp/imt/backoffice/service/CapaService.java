package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Capa}.
 */
public interface CapaService {
    /**
     * Save a capa.
     *
     * @param capa the entity to save.
     * @return the persisted entity.
     */
    Capa save(Capa capa);

    /**
     * Updates a capa.
     *
     * @param capa the entity to update.
     * @return the persisted entity.
     */
    Capa update(Capa capa);

    /**
     * Partially updates a capa.
     *
     * @param capa the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Capa> partialUpdate(Capa capa);

    /**
     * Get all the capas.
     *
     * @return the list of entities.
     */
    List<Capa> findAll();

    /**
     * Get the "id" capa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Capa> findOne(String id);

    /**
     * Delete the "id" capa.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
