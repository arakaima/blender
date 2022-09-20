package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.DossierDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Dossier}.
 */
public interface DossierService {
    /**
     * Save a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    DossierDTO save(DossierDTO dossierDTO);

    /**
     * Updates a dossier.
     *
     * @param dossierDTO the entity to update.
     * @return the persisted entity.
     */
    DossierDTO update(DossierDTO dossierDTO);

    /**
     * Partially updates a dossier.
     *
     * @param dossierDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DossierDTO> partialUpdate(DossierDTO dossierDTO);

    /**
     * Get all the dossiers.
     *
     * @return the list of entities.
     */
    List<DossierDTO> findAll();

    /**
     * Get the "id" dossier.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DossierDTO> findOne(String id);

    /**
     * Delete the "id" dossier.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
