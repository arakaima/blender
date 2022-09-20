package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.ContactPerson;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the ContactPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactPersonRepository extends ReactiveMongoRepository<ContactPerson, String> {}
