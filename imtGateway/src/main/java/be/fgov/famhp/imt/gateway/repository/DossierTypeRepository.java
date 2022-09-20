package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.DossierType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the DossierType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierTypeRepository extends ReactiveMongoRepository<DossierType, String> {}
