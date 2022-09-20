package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the InspectorDossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectorDossierRepository extends MongoRepository<InspectorDossier, String> {}
