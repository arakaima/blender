package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.gateway.service.CapaDocumentService;
import be.fgov.famhp.imt.gateway.service.dto.CapaDocumentDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.CapaDocument}.
 */
@RestController
@RequestMapping("/api")
public class CapaDocumentResource {

    private final Logger log = LoggerFactory.getLogger(CapaDocumentResource.class);

    private static final String ENTITY_NAME = "capaDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapaDocumentService capaDocumentService;

    private final CapaDocumentRepository capaDocumentRepository;

    public CapaDocumentResource(CapaDocumentService capaDocumentService, CapaDocumentRepository capaDocumentRepository) {
        this.capaDocumentService = capaDocumentService;
        this.capaDocumentRepository = capaDocumentRepository;
    }

    /**
     * {@code POST  /capa-documents} : Create a new capaDocument.
     *
     * @param capaDocumentDTO the capaDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capaDocumentDTO, or with status {@code 400 (Bad Request)} if the capaDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capa-documents")
    public Mono<ResponseEntity<CapaDocumentDTO>> createCapaDocument(@RequestBody CapaDocumentDTO capaDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save CapaDocument : {}", capaDocumentDTO);
        if (capaDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new capaDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return capaDocumentService
            .save(capaDocumentDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/capa-documents/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /capa-documents/:id} : Updates an existing capaDocument.
     *
     * @param id the id of the capaDocumentDTO to save.
     * @param capaDocumentDTO the capaDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the capaDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capaDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capa-documents/{id}")
    public Mono<ResponseEntity<CapaDocumentDTO>> updateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocumentDTO capaDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CapaDocument : {}, {}", id, capaDocumentDTO);
        if (capaDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capaDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return capaDocumentService
                    .update(capaDocumentDTO)
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
     * {@code PATCH  /capa-documents/:id} : Partial updates given fields of an existing capaDocument, field will ignore if it is null
     *
     * @param id the id of the capaDocumentDTO to save.
     * @param capaDocumentDTO the capaDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the capaDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the capaDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the capaDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capa-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CapaDocumentDTO>> partialUpdateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocumentDTO capaDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapaDocument partially : {}, {}", id, capaDocumentDTO);
        if (capaDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capaDocumentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CapaDocumentDTO> result = capaDocumentService.partialUpdate(capaDocumentDTO);

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
     * {@code GET  /capa-documents} : get all the capaDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capaDocuments in body.
     */
    @GetMapping("/capa-documents")
    public Mono<List<CapaDocumentDTO>> getAllCapaDocuments() {
        log.debug("REST request to get all CapaDocuments");
        return capaDocumentService.findAll().collectList();
    }

    /**
     * {@code GET  /capa-documents} : get all the capaDocuments as a stream.
     * @return the {@link Flux} of capaDocuments.
     */
    @GetMapping(value = "/capa-documents", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CapaDocumentDTO> getAllCapaDocumentsAsStream() {
        log.debug("REST request to get all CapaDocuments as a stream");
        return capaDocumentService.findAll();
    }

    /**
     * {@code GET  /capa-documents/:id} : get the "id" capaDocument.
     *
     * @param id the id of the capaDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capaDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capa-documents/{id}")
    public Mono<ResponseEntity<CapaDocumentDTO>> getCapaDocument(@PathVariable String id) {
        log.debug("REST request to get CapaDocument : {}", id);
        Mono<CapaDocumentDTO> capaDocumentDTO = capaDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capaDocumentDTO);
    }

    /**
     * {@code DELETE  /capa-documents/:id} : delete the "id" capaDocument.
     *
     * @param id the id of the capaDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capa-documents/{id}")
    public Mono<ResponseEntity<Void>> deleteCapaDocument(@PathVariable String id) {
        log.debug("REST request to delete CapaDocument : {}", id);
        return capaDocumentService
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
