package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Requests;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Requests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestsRepository extends ReactiveMongoRepository<Requests, String> {}
