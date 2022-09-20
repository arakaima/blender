package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Note;
import be.fgov.famhp.imt.backoffice.repository.NoteRepository;
import be.fgov.famhp.imt.backoffice.service.NoteService;
import be.fgov.famhp.imt.backoffice.service.dto.NoteDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.NoteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    @Override
    public NoteDTO save(NoteDTO noteDTO) {
        log.debug("Request to save Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    @Override
    public NoteDTO update(NoteDTO noteDTO) {
        log.debug("Request to update Note : {}", noteDTO);
        Note note = noteMapper.toEntity(noteDTO);
        // no save call needed as we have no fields that can be updated
        return noteMapper.toDto(note);
    }

    @Override
    public Optional<NoteDTO> partialUpdate(NoteDTO noteDTO) {
        log.debug("Request to partially update Note : {}", noteDTO);

        return noteRepository
            .findById(noteDTO.getId())
            .map(existingNote -> {
                noteMapper.partialUpdate(existingNote, noteDTO);

                return existingNote;
            })
            // .map(noteRepository::save)
            .map(noteMapper::toDto);
    }

    @Override
    public List<NoteDTO> findAll() {
        log.debug("Request to get all Notes");
        return noteRepository.findAll().stream().map(noteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<NoteDTO> findOne(String id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id).map(noteMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
    }
}
