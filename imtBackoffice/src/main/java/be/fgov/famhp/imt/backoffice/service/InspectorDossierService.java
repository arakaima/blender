package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link InspectorDossier}.
 */
public interface InspectorDossierService {
    /**
     * Save a inspectorDossier.
     *
     * @param inspectorDossier the entity to save.
     * @return the persisted entity.
     */
    InspectorDossier save(InspectorDossier inspectorDossier);

    /**
     * Updates a inspectorDossier.
     *
     * @param inspectorDossier the entity to update.
     * @return the persisted entity.
     */
    InspectorDossier update(InspectorDossier inspectorDossier);

    /**
     * Partially updates a inspectorDossier.
     *
     * @param inspectorDossier the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectorDossier> partialUpdate(InspectorDossier inspectorDossier);

    /**
     * Get all the inspectorDossiers.
     *
     * @return the list of entities.
     */
    List<InspectorDossier> findAll();

    /**
     * Get the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectorDossier> findOne(String id);

    /**
     * Delete the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
