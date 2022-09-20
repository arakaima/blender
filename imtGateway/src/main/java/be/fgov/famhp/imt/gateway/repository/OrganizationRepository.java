package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Organization;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Organization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationRepository extends ReactiveMongoRepository<Organization, String> {}
