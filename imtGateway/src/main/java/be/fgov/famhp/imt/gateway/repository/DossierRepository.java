package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Dossier;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Dossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierRepository extends ReactiveMongoRepository<Dossier, String> {}
