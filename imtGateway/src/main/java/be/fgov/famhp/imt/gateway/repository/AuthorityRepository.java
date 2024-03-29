package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Authority;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends ReactiveMongoRepository<Authority, String> {}
