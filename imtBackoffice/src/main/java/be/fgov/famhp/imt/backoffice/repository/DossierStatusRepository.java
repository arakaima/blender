package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DossierStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierStatusRepository extends MongoRepository<DossierStatus, String> {}
