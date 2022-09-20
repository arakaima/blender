package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.RequestsMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the RequestsMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestsMessageRepository extends ReactiveMongoRepository<RequestsMessage, String> {}
