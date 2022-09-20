package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Expert;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Expert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertRepository extends ReactiveMongoRepository<Expert, String> {}
