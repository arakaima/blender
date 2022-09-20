package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.NoteRepository;
import be.fgov.famhp.imt.gateway.service.NoteService;
import be.fgov.famhp.imt.gateway.service.dto.NoteDTO;
import be.fgov.famhp.imt.gateway.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.Note}.
 */
@RestController
@RequestMapping("/api")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteService noteService;

    private final NoteRepository noteRepository;

    public NoteResource(NoteService noteService, NoteRepository noteRepository) {
        this.noteService = noteService;
        this.noteRepository = noteRepository;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param noteDTO the noteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteDTO, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notes")
    public Mono<ResponseEntity<NoteDTO>> createNote(@RequestBody NoteDTO noteDTO) throws URISyntaxException {
        log.debug("REST request to save Note : {}", noteDTO);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return noteService
            .save(noteDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/notes/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /notes/:id} : Updates an existing note.
     *
     * @param id the id of the noteDTO to save.
     * @param noteDTO the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notes/{id}")
    public Mono<ResponseEntity<NoteDTO>> updateNote(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Note : {}, {}", id, noteDTO);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return noteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return noteService
                    .update(noteDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /notes/:id} : Partial updates given fields of an existing note, field will ignore if it is null
     *
     * @param id the id of the noteDTO to save.
     * @param noteDTO the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the noteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<NoteDTO>> partialUpdateNote(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Note partially : {}, {}", id, noteDTO);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, noteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return noteRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<NoteDTO> result = noteService.partialUpdate(noteDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("/notes")
    public Mono<List<NoteDTO>> getAllNotes() {
        log.debug("REST request to get all Notes");
        return noteService.findAll().collectList();
    }

    /**
     * {@code GET  /notes} : get all the notes as a stream.
     * @return the {@link Flux} of notes.
     */
    @GetMapping(value = "/notes", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<NoteDTO> getAllNotesAsStream() {
        log.debug("REST request to get all Notes as a stream");
        return noteService.findAll();
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the noteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notes/{id}")
    public Mono<ResponseEntity<NoteDTO>> getNote(@PathVariable String id) {
        log.debug("REST request to get Note : {}", id);
        Mono<NoteDTO> noteDTO = noteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(noteDTO);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param id the id of the noteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notes/{id}")
    public Mono<ResponseEntity<Void>> deleteNote(@PathVariable String id) {
        log.debug("REST request to delete Note : {}", id);
        return noteService
            .delete(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id))
                        .build()
                )
            );
    }
}
