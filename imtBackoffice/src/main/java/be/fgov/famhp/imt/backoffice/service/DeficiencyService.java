package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.DeficiencyDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Deficiency}.
 */
public interface DeficiencyService {
    /**
     * Save a deficiency.
     *
     * @param deficiencyDTO the entity to save.
     * @return the persisted entity.
     */
    DeficiencyDTO save(DeficiencyDTO deficiencyDTO);

    /**
     * Updates a deficiency.
     *
     * @param deficiencyDTO the entity to update.
     * @return the persisted entity.
     */
    DeficiencyDTO update(DeficiencyDTO deficiencyDTO);

    /**
     * Partially updates a deficiency.
     *
     * @param deficiencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeficiencyDTO> partialUpdate(DeficiencyDTO deficiencyDTO);

    /**
     * Get all the deficiencies.
     *
     * @return the list of entities.
     */
    List<DeficiencyDTO> findAll();

    /**
     * Get the "id" deficiency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeficiencyDTO> findOne(String id);

    /**
     * Delete the "id" deficiency.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
