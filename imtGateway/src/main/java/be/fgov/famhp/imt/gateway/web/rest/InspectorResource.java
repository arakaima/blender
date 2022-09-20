package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.InspectorRepository;
import be.fgov.famhp.imt.gateway.service.InspectorService;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.Inspector}.
 */
@RestController
@RequestMapping("/api")
public class InspectorResource {

    private final Logger log = LoggerFactory.getLogger(InspectorResource.class);

    private static final String ENTITY_NAME = "inspector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectorService inspectorService;

    private final InspectorRepository inspectorRepository;

    public InspectorResource(InspectorService inspectorService, InspectorRepository inspectorRepository) {
        this.inspectorService = inspectorService;
        this.inspectorRepository = inspectorRepository;
    }

    /**
     * {@code POST  /inspectors} : Create a new inspector.
     *
     * @param inspectorDTO the inspectorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectorDTO, or with status {@code 400 (Bad Request)} if the inspector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspectors")
    public Mono<ResponseEntity<InspectorDTO>> createInspector(@RequestBody InspectorDTO inspectorDTO) throws URISyntaxException {
        log.debug("REST request to save Inspector : {}", inspectorDTO);
        if (inspectorDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return inspectorService
            .save(inspectorDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/inspectors/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /inspectors/:id} : Updates an existing inspector.
     *
     * @param id the id of the inspectorDTO to save.
     * @param inspectorDTO the inspectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspectors/{id}")
    public Mono<ResponseEntity<InspectorDTO>> updateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDTO inspectorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Inspector : {}, {}", id, inspectorDTO);
        if (inspectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return inspectorService
                    .update(inspectorDTO)
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
     * {@code PATCH  /inspectors/:id} : Partial updates given fields of an existing inspector, field will ignore if it is null
     *
     * @param id the id of the inspectorDTO to save.
     * @param inspectorDTO the inspectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspectors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InspectorDTO>> partialUpdateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDTO inspectorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspector partially : {}, {}", id, inspectorDTO);
        if (inspectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectorRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InspectorDTO> result = inspectorService.partialUpdate(inspectorDTO);

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
     * {@code GET  /inspectors} : get all the inspectors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectors in body.
     */
    @GetMapping("/inspectors")
    public Mono<List<InspectorDTO>> getAllInspectors() {
        log.debug("REST request to get all Inspectors");
        return inspectorService.findAll().collectList();
    }

    /**
     * {@code GET  /inspectors} : get all the inspectors as a stream.
     * @return the {@link Flux} of inspectors.
     */
    @GetMapping(value = "/inspectors", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<InspectorDTO> getAllInspectorsAsStream() {
        log.debug("REST request to get all Inspectors as a stream");
        return inspectorService.findAll();
    }

    /**
     * {@code GET  /inspectors/:id} : get the "id" inspector.
     *
     * @param id the id of the inspectorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspectors/{id}")
    public Mono<ResponseEntity<InspectorDTO>> getInspector(@PathVariable String id) {
        log.debug("REST request to get Inspector : {}", id);
        Mono<InspectorDTO> inspectorDTO = inspectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectorDTO);
    }

    /**
     * {@code DELETE  /inspectors/:id} : delete the "id" inspector.
     *
     * @param id the id of the inspectorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspectors/{id}")
    public Mono<ResponseEntity<Void>> deleteInspector(@PathVariable String id) {
        log.debug("REST request to delete Inspector : {}", id);
        return inspectorService
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
