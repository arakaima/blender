package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Note;
import be.fgov.famhp.imt.gateway.repository.NoteRepository;
import be.fgov.famhp.imt.gateway.service.NoteService;
import be.fgov.famhp.imt.gateway.service.dto.NoteDTO;
import be.fgov.famhp.imt.gateway.service.mapper.NoteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<NoteDTO> save(NoteDTO noteDTO) {
        log.debug("Request to save Note : {}", noteDTO);
        return noteRepository.save(noteMapper.toEntity(noteDTO)).map(noteMapper::toDto);
    }

    @Override
    public Mono<NoteDTO> update(NoteDTO noteDTO) {
        log.debug("Request to update Note : {}", noteDTO);
        // no save call needed as we have no fields that can be updated
        return noteRepository.findById(noteDTO.getId()).map(noteMapper::toDto);
    }

    @Override
    public Mono<NoteDTO> partialUpdate(NoteDTO noteDTO) {
        log.debug("Request to partially update Note : {}", noteDTO);

        return noteRepository
            .findById(noteDTO.getId())
            .map(existingNote -> {
                noteMapper.partialUpdate(existingNote, noteDTO);

                return existingNote;
            })
            // .flatMap(noteRepository::save)
            .map(noteMapper::toDto);
    }

    @Override
    public Flux<NoteDTO> findAll() {
        log.debug("Request to get all Notes");
        return noteRepository.findAll().map(noteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return noteRepository.count();
    }

    @Override
    public Mono<NoteDTO> findOne(String id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id).map(noteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Note : {}", id);
        return noteRepository.deleteById(id);
    }
}
