package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.DossierType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the DossierType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierTypeRepository extends MongoRepository<DossierType, String> {}
