package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Dossier;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Dossier}.
 */
public interface DossierService {
    /**
     * Save a dossier.
     *
     * @param dossier the entity to save.
     * @return the persisted entity.
     */
    Dossier save(Dossier dossier);

    /**
     * Updates a dossier.
     *
     * @param dossier the entity to update.
     * @return the persisted entity.
     */
    Dossier update(Dossier dossier);

    /**
     * Partially updates a dossier.
     *
     * @param dossier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Dossier> partialUpdate(Dossier dossier);

    /**
     * Get all the dossiers.
     *
     * @return the list of entities.
     */
    List<Dossier> findAll();

    /**
     * Get the "id" dossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Dossier> findOne(String id);

    /**
     * Delete the "id" dossier.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
