package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Expert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Expert entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExpertRepository extends MongoRepository<Expert, String> {}
