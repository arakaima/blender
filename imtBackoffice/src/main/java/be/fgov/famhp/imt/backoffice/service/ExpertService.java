package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.ExpertDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Expert}.
 */
public interface ExpertService {
    /**
     * Save a expert.
     *
     * @param expertDTO the entity to save.
     * @return the persisted entity.
     */
    ExpertDTO save(ExpertDTO expertDTO);

    /**
     * Updates a expert.
     *
     * @param expertDTO the entity to update.
     * @return the persisted entity.
     */
    ExpertDTO update(ExpertDTO expertDTO);

    /**
     * Partially updates a expert.
     *
     * @param expertDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ExpertDTO> partialUpdate(ExpertDTO expertDTO);

    /**
     * Get all the experts.
     *
     * @return the list of entities.
     */
    List<ExpertDTO> findAll();

    /**
     * Get the "id" expert.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExpertDTO> findOne(String id);

    /**
     * Delete the "id" expert.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
