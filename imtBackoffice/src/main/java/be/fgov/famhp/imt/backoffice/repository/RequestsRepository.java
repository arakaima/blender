package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Requests entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestsRepository extends MongoRepository<Requests, String> {}
