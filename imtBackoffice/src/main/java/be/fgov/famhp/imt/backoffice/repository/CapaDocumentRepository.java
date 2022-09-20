package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the CapaDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapaDocumentRepository extends MongoRepository<CapaDocument, String> {}
