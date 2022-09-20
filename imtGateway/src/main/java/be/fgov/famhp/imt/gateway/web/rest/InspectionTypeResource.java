package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.gateway.service.InspectionTypeService;
import be.fgov.famhp.imt.gateway.service.dto.InspectionTypeDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.InspectionType}.
 */
@RestController
@RequestMapping("/api")
public class InspectionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InspectionTypeResource.class);

    private static final String ENTITY_NAME = "inspectionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectionTypeService inspectionTypeService;

    private final InspectionTypeRepository inspectionTypeRepository;

    public InspectionTypeResource(InspectionTypeService inspectionTypeService, InspectionTypeRepository inspectionTypeRepository) {
        this.inspectionTypeService = inspectionTypeService;
        this.inspectionTypeRepository = inspectionTypeRepository;
    }

    /**
     * {@code POST  /inspection-types} : Create a new inspectionType.
     *
     * @param inspectionTypeDTO the inspectionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectionTypeDTO, or with status {@code 400 (Bad Request)} if the inspectionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspection-types")
    public Mono<ResponseEntity<InspectionTypeDTO>> createInspectionType(@RequestBody InspectionTypeDTO inspectionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save InspectionType : {}", inspectionTypeDTO);
        if (inspectionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspectionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return inspectionTypeService
            .save(inspectionTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/inspection-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /inspection-types/:id} : Updates an existing inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to save.
     * @param inspectionTypeDTO the inspectionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspection-types/{id}")
    public Mono<ResponseEntity<InspectionTypeDTO>> updateInspectionType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionTypeDTO inspectionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InspectionType : {}, {}", id, inspectionTypeDTO);
        if (inspectionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return inspectionTypeService
                    .update(inspectionTypeDTO)
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
     * {@code PATCH  /inspection-types/:id} : Partial updates given fields of an existing inspectionType, field will ignore if it is null
     *
     * @param id the id of the inspectionTypeDTO to save.
     * @param inspectionTypeDTO the inspectionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspection-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<InspectionTypeDTO>> partialUpdateInspectionType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionTypeDTO inspectionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InspectionType partially : {}, {}", id, inspectionTypeDTO);
        if (inspectionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inspectionTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<InspectionTypeDTO> result = inspectionTypeService.partialUpdate(inspectionTypeDTO);

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
     * {@code GET  /inspection-types} : get all the inspectionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectionTypes in body.
     */
    @GetMapping("/inspection-types")
    public Mono<List<InspectionTypeDTO>> getAllInspectionTypes() {
        log.debug("REST request to get all InspectionTypes");
        return inspectionTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /inspection-types} : get all the inspectionTypes as a stream.
     * @return the {@link Flux} of inspectionTypes.
     */
    @GetMapping(value = "/inspection-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<InspectionTypeDTO> getAllInspectionTypesAsStream() {
        log.debug("REST request to get all InspectionTypes as a stream");
        return inspectionTypeService.findAll();
    }

    /**
     * {@code GET  /inspection-types/:id} : get the "id" inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspection-types/{id}")
    public Mono<ResponseEntity<InspectionTypeDTO>> getInspectionType(@PathVariable String id) {
        log.debug("REST request to get InspectionType : {}", id);
        Mono<InspectionTypeDTO> inspectionTypeDTO = inspectionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectionTypeDTO);
    }

    /**
     * {@code DELETE  /inspection-types/:id} : delete the "id" inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspection-types/{id}")
    public Mono<ResponseEntity<Void>> deleteInspectionType(@PathVariable String id) {
        log.debug("REST request to delete InspectionType : {}", id);
        return inspectionTypeService
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
