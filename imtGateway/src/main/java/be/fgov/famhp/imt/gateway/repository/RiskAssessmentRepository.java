package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.RiskAssessment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the RiskAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskAssessmentRepository extends ReactiveMongoRepository<RiskAssessment, String> {}
