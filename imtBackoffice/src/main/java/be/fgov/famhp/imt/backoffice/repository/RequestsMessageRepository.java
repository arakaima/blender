package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the RequestsMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestsMessageRepository extends MongoRepository<RequestsMessage, String> {}
