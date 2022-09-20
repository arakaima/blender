package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.DossierType;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DossierType}.
 */
public interface DossierTypeService {
    /**
     * Save a dossierType.
     *
     * @param dossierType the entity to save.
     * @return the persisted entity.
     */
    DossierType save(DossierType dossierType);

    /**
     * Updates a dossierType.
     *
     * @param dossierType the entity to update.
     * @return the persisted entity.
     */
    DossierType update(DossierType dossierType);

    /**
     * Partially updates a dossierType.
     *
     * @param dossierType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierType> partialUpdate(DossierType dossierType);

    /**
     * Get all the dossierTypes.
     *
     * @return the list of entities.
     */
    List<DossierType> findAll();

    /**
     * Get the "id" dossierType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierType> findOne(String id);

    /**
     * Delete the "id" dossierType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
