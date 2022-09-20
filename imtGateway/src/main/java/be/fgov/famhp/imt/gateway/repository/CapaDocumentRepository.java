package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.CapaDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the CapaDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapaDocumentRepository extends ReactiveMongoRepository<CapaDocument, String> {}
