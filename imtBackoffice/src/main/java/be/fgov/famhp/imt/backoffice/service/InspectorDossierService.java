package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.InspectorDossierDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectorDossier}.
 */
public interface InspectorDossierService {
    /**
     * Save a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to save.
     * @return the persisted entity.
     */
    InspectorDossierDTO save(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Updates a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to update.
     * @return the persisted entity.
     */
    InspectorDossierDTO update(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Partially updates a inspectorDossier.
     *
     * @param inspectorDossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectorDossierDTO> partialUpdate(InspectorDossierDTO inspectorDossierDTO);

    /**
     * Get all the inspectorDossiers.
     *
     * @return the list of entities.
     */
    List<InspectorDossierDTO> findAll();

    /**
     * Get the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectorDossierDTO> findOne(String id);

    /**
     * Delete the "id" inspectorDossier.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
