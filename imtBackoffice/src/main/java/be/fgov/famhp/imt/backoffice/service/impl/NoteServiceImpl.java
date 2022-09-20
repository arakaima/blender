package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Note;
import be.fgov.famhp.imt.backoffice.repository.NoteRepository;
import be.fgov.famhp.imt.backoffice.service.NoteService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        return noteRepository.save(note);
    }

    @Override
    public Note update(Note note) {
        log.debug("Request to update Note : {}", note);
        return noteRepository.save(note);
    }

    @Override
    public Optional<Note> partialUpdate(Note note) {
        log.debug("Request to partially update Note : {}", note);

        return noteRepository
            .findById(note.getId())
            .map(existingNote -> {
                if (note.getNoteNumber() != null) {
                    existingNote.setNoteNumber(note.getNoteNumber());
                }
                if (note.getSendDate() != null) {
                    existingNote.setSendDate(note.getSendDate());
                }
                if (note.getLabel() != null) {
                    existingNote.setLabel(note.getLabel());
                }
                if (note.getStatus() != null) {
                    existingNote.setStatus(note.getStatus());
                }

                return existingNote;
            })
            .map(noteRepository::save);
    }

    @Override
    public List<Note> findAll() {
        log.debug("Request to get all Notes");
        return noteRepository.findAll();
    }

    @Override
    public Optional<Note> findOne(String id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
