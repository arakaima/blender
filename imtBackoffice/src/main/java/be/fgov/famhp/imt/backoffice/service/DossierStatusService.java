package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.DossierStatusDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.DossierStatus}.
 */
public interface DossierStatusService {
    /**
     * Save a dossierStatus.
     *
     * @param dossierStatusDTO the entity to save.
     * @return the persisted entity.
     */
    DossierStatusDTO save(DossierStatusDTO dossierStatusDTO);

    /**
     * Updates a dossierStatus.
     *
     * @param dossierStatusDTO the entity to update.
     * @return the persisted entity.
     */
    DossierStatusDTO update(DossierStatusDTO dossierStatusDTO);

    /**
     * Partially updates a dossierStatus.
     *
     * @param dossierStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierStatusDTO> partialUpdate(DossierStatusDTO dossierStatusDTO);

    /**
     * Get all the dossierStatuses.
     *
     * @return the list of entities.
     */
    List<DossierStatusDTO> findAll();

    /**
     * Get the "id" dossierStatus.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierStatusDTO> findOne(String id);

    /**
     * Delete the "id" dossierStatus.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
