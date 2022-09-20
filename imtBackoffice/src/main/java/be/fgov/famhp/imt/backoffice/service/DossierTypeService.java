package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.DossierTypeDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.DossierType}.
 */
public interface DossierTypeService {
    /**
     * Save a dossierType.
     *
     * @param dossierTypeDTO the entity to save.
     * @return the persisted entity.
     */
    DossierTypeDTO save(DossierTypeDTO dossierTypeDTO);

    /**
     * Updates a dossierType.
     *
     * @param dossierTypeDTO the entity to update.
     * @return the persisted entity.
     */
    DossierTypeDTO update(DossierTypeDTO dossierTypeDTO);

    /**
     * Partially updates a dossierType.
     *
     * @param dossierTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierTypeDTO> partialUpdate(DossierTypeDTO dossierTypeDTO);

    /**
     * Get all the dossierTypes.
     *
     * @return the list of entities.
     */
    List<DossierTypeDTO> findAll();

    /**
     * Get the "id" dossierType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierTypeDTO> findOne(String id);

    /**
     * Delete the "id" dossierType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
