package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Capa entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CapaRepository extends MongoRepository<Capa, String> {}
