package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Capa;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Capa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapaRepository extends ReactiveMongoRepository<Capa, String> {}
