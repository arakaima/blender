package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the AttachedDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachedDocumentRepository extends MongoRepository<AttachedDocument, String> {}
