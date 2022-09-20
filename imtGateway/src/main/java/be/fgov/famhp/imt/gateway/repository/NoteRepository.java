package be.fgov.famhp.imt.gateway.repository;

import be.fgov.famhp.imt.gateway.domain.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends ReactiveMongoRepository<Note, String> {}
