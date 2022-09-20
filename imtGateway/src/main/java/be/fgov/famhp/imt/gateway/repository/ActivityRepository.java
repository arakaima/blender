package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Activity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Activity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityRepository extends ReactiveMongoRepository<Activity, String> {}
