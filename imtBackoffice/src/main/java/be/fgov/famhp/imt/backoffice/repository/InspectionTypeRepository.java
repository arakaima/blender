package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the InspectionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectionTypeRepository extends MongoRepository<InspectionType, String> {}
