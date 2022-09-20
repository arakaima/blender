package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.AttachedDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the AttachedDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachedDocumentRepository extends ReactiveMongoRepository<AttachedDocument, String> {}
