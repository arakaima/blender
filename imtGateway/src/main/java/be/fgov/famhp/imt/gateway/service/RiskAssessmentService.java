package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.RiskAssessmentDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.RiskAssessment}.
 */
public interface RiskAssessmentService {
    /**
     * Save a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<RiskAssessmentDTO> save(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Updates a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<RiskAssessmentDTO> update(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Partially updates a riskAssessment.
     *
     * @param riskAssessmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<RiskAssessmentDTO> partialUpdate(RiskAssessmentDTO riskAssessmentDTO);

    /**
     * Get all the riskAssessments.
     *
     * @return the list of entities.
     */
    Flux<RiskAssessmentDTO> findAll();

    /**
     * Returns the number of riskAssessments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" riskAssessment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<RiskAssessmentDTO> findOne(String id);

    /**
     * Delete the "id" riskAssessment.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
