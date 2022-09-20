package be.fgov.famhp.imt.backoffice.repository;

import be.fgov.famhp.imt.backoffice.domain.Site;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Site entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteRepository extends MongoRepository<Site, String> {}
