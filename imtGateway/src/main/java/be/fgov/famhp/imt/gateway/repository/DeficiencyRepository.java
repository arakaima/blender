package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Deficiency;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Deficiency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeficiencyRepository extends ReactiveMongoRepository<Deficiency, String> {}
