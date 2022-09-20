package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Note;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Note}.
 */
public interface NoteService {
    /**
     * Save a note.
     *
     * @param note the entity to save.
     * @return the persisted entity.
     */
    Note save(Note note);

    /**
     * Updates a note.
     *
     * @param note the entity to update.
     * @return the persisted entity.
     */
    Note update(Note note);

    /**
     * Partially updates a note.
     *
     * @param note the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Note> partialUpdate(Note note);

    /**
     * Get all the notes.
     *
     * @return the list of entities.
     */
    List<Note> findAll();

    /**
     * Get the "id" note.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Note> findOne(String id);

    /**
     * Delete the "id" note.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
