package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.OrganizationDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the OrganizationDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationDocumentRepository extends ReactiveMongoRepository<OrganizationDocument, String> {}
