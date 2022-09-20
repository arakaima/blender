package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Message;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Message entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageRepository extends ReactiveMongoRepository<Message, String> {}
