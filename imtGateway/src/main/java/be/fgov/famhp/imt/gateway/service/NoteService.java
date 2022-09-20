package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.NoteDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param noteDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<NoteDTO> save(NoteDTO noteDTO);

    /**
     * Updates a note.
     *
     * @param noteDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<NoteDTO> update(NoteDTO noteDTO);

    /**
     * Partially updates a note.
     *
     * @param noteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<NoteDTO> partialUpdate(NoteDTO noteDTO);

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    Flux<NoteDTO> findAll();

    /**
     * Returns the number of notes available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<NoteDTO> findOne(String id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
