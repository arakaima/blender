package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Authority;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Spring Data MongoDB repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends MongoRepository<Authority, String> {}
