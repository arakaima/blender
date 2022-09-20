package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the ContactPerson entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactPersonRepository extends MongoRepository<ContactPerson, String> {}
