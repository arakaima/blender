package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.InspectorDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Inspector}.
 */
public interface InspectorService {
    /**
     * Save a inspector.
     *
     * @param inspectorDTO the entity to save.
     * @return the persisted entity.
     */
    InspectorDTO save(InspectorDTO inspectorDTO);

    /**
     * Updates a inspector.
     *
     * @param inspectorDTO the entity to update.
     * @return the persisted entity.
     */
    InspectorDTO update(InspectorDTO inspectorDTO);

    /**
     * Partially updates a inspector.
     *
     * @param inspectorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<InspectorDTO> partialUpdate(InspectorDTO inspectorDTO);

    /**
     * Get all the inspectors.
     *
     * @return the list of entities.
     */
    List<InspectorDTO> findAll();

    /**
     * Get the "id" inspector.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<InspectorDTO> findOne(String id);

    /**
     * Delete the "id" inspector.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
