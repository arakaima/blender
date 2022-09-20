package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link DossierStatus}.
 */
public interface DossierStatusService {
    /**
     * Save a dossierStatus.
     *
     * @param dossierStatus the entity to save.
     * @return the persisted entity.
     */
    DossierStatus save(DossierStatus dossierStatus);

    /**
     * Updates a dossierStatus.
     *
     * @param dossierStatus the entity to update.
     * @return the persisted entity.
     */
    DossierStatus update(DossierStatus dossierStatus);

    /**
     * Partially updates a dossierStatus.
     *
     * @param dossierStatus the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierStatus> partialUpdate(DossierStatus dossierStatus);

    /**
     * Get all the dossierStatuses.
     *
     * @return the list of entities.
     */
    List<DossierStatus> findAll();

    /**
     * Get the "id" dossierStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierStatus> findOne(String id);

    /**
     * Delete the "id" dossierStatus.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
