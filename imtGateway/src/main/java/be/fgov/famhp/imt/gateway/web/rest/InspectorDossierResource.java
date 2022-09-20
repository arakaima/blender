package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.gateway.service.InspectorDossierService;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDossierDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.InspectorDossier}.
 */
@RestController
@RequestMapping("/api")
public class InspectorDossierResource {

    private final Logger log = LoggerFactory.getLogger(InspectorDossierResource.class);

    private static final String ENTITY_NAME = "inspectorDossier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectorDossierService inspectorDossierService;

    private final InspectorDossierRepository inspectorDossierRepository;

    public InspectorDossierResource(
        InspectorDossierService inspectorDossierService,
        InspectorDossierRepository inspectorDossierRepository
    ) {
        this.inspectorDossierService = inspectorDossierService;
        this.inspectorDossierRepository = inspectorDossierRepository;
    }

    /**
     * {@code POST  /inspector-dossiers} : Create a new inspectorDossier.
     *
     * @param inspectorDossierDTO the inspectorDossierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectorDossierDTO, or with status {@code 400 (Bad Request)} if the inspectorDossier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspector-dossiers")
    public Mono<ResponseEntity<InspectorDossierDTO>> createInspectorDossier(@RequestBody InspectorDossierDTO inspectorDossierDTO)
        throws URISyntaxException {
        log.debug("REST request to save InspectorDossier : {}", inspectorDossierDTO);
        if (inspectorDossierDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspectorDossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return inspectorDossierService
            .save(inspectorDossierDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/inspector-dossiers/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /inspector-dossiers/:id} : Updates an existing inspectorDossier.
     *
     * @param id the id of the inspectorDossierDTO to save.
     * @param inspectorDossierDTO the inspectorDossierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDossierDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDossierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDossierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspector-dossiers/{id}")
    public Mono<ResponseEntity<InspectorDossierDTO>> updateInspectorDossier(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDossierDTO inspectorDossierDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InspectorDossier : {}, {}", id, inspectorDossierDTO);
        if (inspectorDossierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDossierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectorDossierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return inspectorDossierService
                    .update(inspectorDossierDTO)
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
     * {@code PATCH  /inspector-dossiers/:id} : Partial updates given fields of an existing inspectorDossier, field will ignore if it is null
     *
     * @param id the id of the inspectorDossierDTO to save.
     * @param inspectorDossierDTO the inspectorDossierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDossierDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDossierDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectorDossierDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDossierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspector-dossiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InspectorDossierDTO>> partialUpdateInspectorDossier(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDossierDTO inspectorDossierDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InspectorDossier partially : {}, {}", id, inspectorDossierDTO);
        if (inspectorDossierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDossierDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectorDossierRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InspectorDossierDTO> result = inspectorDossierService.partialUpdate(inspectorDossierDTO);

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
     * {@code GET  /inspector-dossiers} : get all the inspectorDossiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectorDossiers in body.
     */
    @GetMapping("/inspector-dossiers")
    public Mono<List<InspectorDossierDTO>> getAllInspectorDossiers() {
        log.debug("REST request to get all InspectorDossiers");
        return inspectorDossierService.findAll().collectList();
    }

    /**
     * {@code GET  /inspector-dossiers} : get all the inspectorDossiers as a stream.
     * @return the {@link Flux} of inspectorDossiers.
     */
    @GetMapping(value = "/inspector-dossiers", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<InspectorDossierDTO> getAllInspectorDossiersAsStream() {
        log.debug("REST request to get all InspectorDossiers as a stream");
        return inspectorDossierService.findAll();
    }

    /**
     * {@code GET  /inspector-dossiers/:id} : get the "id" inspectorDossier.
     *
     * @param id the id of the inspectorDossierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectorDossierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspector-dossiers/{id}")
    public Mono<ResponseEntity<InspectorDossierDTO>> getInspectorDossier(@PathVariable String id) {
        log.debug("REST request to get InspectorDossier : {}", id);
        Mono<InspectorDossierDTO> inspectorDossierDTO = inspectorDossierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectorDossierDTO);
    }

    /**
     * {@code DELETE  /inspector-dossiers/:id} : delete the "id" inspectorDossier.
     *
     * @param id the id of the inspectorDossierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspector-dossiers/{id}")
    public Mono<ResponseEntity<Void>> deleteInspectorDossier(@PathVariable String id) {
        log.debug("REST request to delete InspectorDossier : {}", id);
        return inspectorDossierService
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
