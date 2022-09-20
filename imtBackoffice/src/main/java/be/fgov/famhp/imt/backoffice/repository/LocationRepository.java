package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Location;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends MongoRepository<Location, String> {}