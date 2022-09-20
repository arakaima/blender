package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the RiskAssessment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiskAssessmentRepository extends MongoRepository<RiskAssessment, String> {}
