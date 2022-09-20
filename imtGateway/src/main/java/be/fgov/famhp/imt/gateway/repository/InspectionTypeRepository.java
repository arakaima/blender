package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.InspectionType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the InspectionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InspectionTypeRepository extends ReactiveMongoRepository<InspectionType, String> {}
