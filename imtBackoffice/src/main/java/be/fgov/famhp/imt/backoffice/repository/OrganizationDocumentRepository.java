package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the OrganizationDocument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationDocumentRepository extends MongoRepository<OrganizationDocument, String> {}
