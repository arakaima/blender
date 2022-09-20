package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Inspector;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Inspector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectorRepository extends ReactiveMongoRepository<Inspector, String> {}
