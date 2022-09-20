package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link RiskAssessment}.
 */
public interface RiskAssessmentService {
    /**
     * Save a riskAssessment.
     *
     * @param riskAssessment the entity to save.
     * @return the persisted entity.
     */
    RiskAssessment save(RiskAssessment riskAssessment);

    /**
     * Updates a riskAssessment.
     *
     * @param riskAssessment the entity to update.
     * @return the persisted entity.
     */
    RiskAssessment update(RiskAssessment riskAssessment);

    /**
     * Partially updates a riskAssessment.
     *
     * @param riskAssessment the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RiskAssessment> partialUpdate(RiskAssessment riskAssessment);

    /**
     * Get all the riskAssessments.
     *
     * @return the list of entities.
     */
    List<RiskAssessment> findAll();

    /**
     * Get the "id" riskAssessment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RiskAssessment> findOne(String id);

    /**
     * Delete the "id" riskAssessment.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
