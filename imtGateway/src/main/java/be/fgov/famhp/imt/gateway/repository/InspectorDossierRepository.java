package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.InspectorDossier;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the InspectorDossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectorDossierRepository extends ReactiveMongoRepository<InspectorDossier, String> {}
