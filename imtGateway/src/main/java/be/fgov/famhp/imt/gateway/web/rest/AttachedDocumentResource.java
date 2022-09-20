package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.gateway.service.AttachedDocumentService;
import be.fgov.famhp.imt.gateway.service.dto.AttachedDocumentDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.AttachedDocument}.
 */
@RestController
@RequestMapping("/api")
public class AttachedDocumentResource {

    private final Logger log = LoggerFactory.getLogger(AttachedDocumentResource.class);

    private static final String ENTITY_NAME = "attachedDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachedDocumentService attachedDocumentService;

    private final AttachedDocumentRepository attachedDocumentRepository;

    public AttachedDocumentResource(
        AttachedDocumentService attachedDocumentService,
        AttachedDocumentRepository attachedDocumentRepository
    ) {
        this.attachedDocumentService = attachedDocumentService;
        this.attachedDocumentRepository = attachedDocumentRepository;
    }

    /**
     * {@code POST  /attached-documents} : Create a new attachedDocument.
     *
     * @param attachedDocumentDTO the attachedDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachedDocumentDTO, or with status {@code 400 (Bad Request)} if the attachedDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attached-documents")
    public Mono<ResponseEntity<AttachedDocumentDTO>> createAttachedDocument(@RequestBody AttachedDocumentDTO attachedDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save AttachedDocument : {}", attachedDocumentDTO);
        if (attachedDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new attachedDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return attachedDocumentService
            .save(attachedDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/attached-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /attached-documents/:id} : Updates an existing attachedDocument.
     *
     * @param id the id of the attachedDocumentDTO to save.
     * @param attachedDocumentDTO the attachedDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the attachedDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachedDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attached-documents/{id}")
    public Mono<ResponseEntity<AttachedDocumentDTO>> updateAttachedDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttachedDocumentDTO attachedDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AttachedDocument : {}, {}", id, attachedDocumentDTO);
        if (attachedDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attachedDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return attachedDocumentService
                    .update(attachedDocumentDTO)
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
     * {@code PATCH  /attached-documents/:id} : Partial updates given fields of an existing attachedDocument, field will ignore if it is null
     *
     * @param id the id of the attachedDocumentDTO to save.
     * @param attachedDocumentDTO the attachedDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the attachedDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the attachedDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachedDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attached-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<AttachedDocumentDTO>> partialUpdateAttachedDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttachedDocumentDTO attachedDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttachedDocument partially : {}, {}", id, attachedDocumentDTO);
        if (attachedDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attachedDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<AttachedDocumentDTO> result = attachedDocumentService.partialUpdate(attachedDocumentDTO);

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
     * {@code GET  /attached-documents} : get all the attachedDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachedDocuments in body.
     */
    @GetMapping("/attached-documents")
    public Mono<List<AttachedDocumentDTO>> getAllAttachedDocuments() {
        log.debug("REST request to get all AttachedDocuments");
        return attachedDocumentService.findAll().collectList();
    }

    /**
     * {@code GET  /attached-documents} : get all the attachedDocuments as a stream.
     * @return the {@link Flux} of attachedDocuments.
     */
    @GetMapping(value = "/attached-documents", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AttachedDocumentDTO> getAllAttachedDocumentsAsStream() {
        log.debug("REST request to get all AttachedDocuments as a stream");
        return attachedDocumentService.findAll();
    }

    /**
     * {@code GET  /attached-documents/:id} : get the "id" attachedDocument.
     *
     * @param id the id of the attachedDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachedDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attached-documents/{id}")
    public Mono<ResponseEntity<AttachedDocumentDTO>> getAttachedDocument(@PathVariable String id) {
        log.debug("REST request to get AttachedDocument : {}", id);
        Mono<AttachedDocumentDTO> attachedDocumentDTO = attachedDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachedDocumentDTO);
    }

    /**
     * {@code DELETE  /attached-documents/:id} : delete the "id" attachedDocument.
     *
     * @param id the id of the attachedDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attached-documents/{id}")
    public Mono<ResponseEntity<Void>> deleteAttachedDocument(@PathVariable String id) {
        log.debug("REST request to delete AttachedDocument : {}", id);
        return attachedDocumentService
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
