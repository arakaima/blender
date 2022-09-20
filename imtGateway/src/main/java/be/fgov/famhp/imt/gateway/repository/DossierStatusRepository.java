package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.DossierStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the DossierStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierStatusRepository extends ReactiveMongoRepository<DossierStatus, String> {}
