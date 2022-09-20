package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.RiskAssessmentDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.RiskAssessment}.
 */
public interface RiskAssessmentService {
    /**
     * Save a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to save.
     * @return the persisted entity.
     */
    RiskAssessmentDTO save(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Updates a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to update.
     * @return the persisted entity.
     */
    RiskAssessmentDTO update(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Partially updates a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RiskAssessmentDTO> partialUpdate(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Get all the riskAssessments.
     *
     * @return the list of entities.
     */
    List<RiskAssessmentDTO> findAll();

    /**
     * Get the "id" riskAssessment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RiskAssessmentDTO> findOne(String id);

    /**
     * Delete the "id" riskAssessment.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
