package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.DossierTypeRepository;
import be.fgov.famhp.imt.gateway.service.DossierTypeService;
import be.fgov.famhp.imt.gateway.service.dto.DossierTypeDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.DossierType}.
 */
@RestController
@RequestMapping("/api")
public class DossierTypeResource {

    private final Logger log = LoggerFactory.getLogger(DossierTypeResource.class);

    private static final String ENTITY_NAME = "dossierType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierTypeService dossierTypeService;

    private final DossierTypeRepository dossierTypeRepository;

    public DossierTypeResource(DossierTypeService dossierTypeService, DossierTypeRepository dossierTypeRepository) {
        this.dossierTypeService = dossierTypeService;
        this.dossierTypeRepository = dossierTypeRepository;
    }

    /**
     * {@code POST  /dossier-types} : Create a new dossierType.
     *
     * @param dossierTypeDTO the dossierTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierTypeDTO, or with status {@code 400 (Bad Request)} if the dossierType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossier-types")
    public Mono<ResponseEntity<DossierTypeDTO>> createDossierType(@RequestBody DossierTypeDTO dossierTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DossierType : {}", dossierTypeDTO);
        if (dossierTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossierType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return dossierTypeService
            .save(dossierTypeDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/dossier-types/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /dossier-types/:id} : Updates an existing dossierType.
     *
     * @param id the id of the dossierTypeDTO to save.
     * @param dossierTypeDTO the dossierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the dossierTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossier-types/{id}")
    public Mono<ResponseEntity<DossierTypeDTO>> updateDossierType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierTypeDTO dossierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DossierType : {}, {}", id, dossierTypeDTO);
        if (dossierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dossierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return dossierTypeService
                    .update(dossierTypeDTO)
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
     * {@code PATCH  /dossier-types/:id} : Partial updates given fields of an existing dossierType, field will ignore if it is null
     *
     * @param id the id of the dossierTypeDTO to save.
     * @param dossierTypeDTO the dossierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the dossierTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dossierTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossier-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<DossierTypeDTO>> partialUpdateDossierType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierTypeDTO dossierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DossierType partially : {}, {}", id, dossierTypeDTO);
        if (dossierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return dossierTypeRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<DossierTypeDTO> result = dossierTypeService.partialUpdate(dossierTypeDTO);

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
     * {@code GET  /dossier-types} : get all the dossierTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierTypes in body.
     */
    @GetMapping("/dossier-types")
    public Mono<List<DossierTypeDTO>> getAllDossierTypes() {
        log.debug("REST request to get all DossierTypes");
        return dossierTypeService.findAll().collectList();
    }

    /**
     * {@code GET  /dossier-types} : get all the dossierTypes as a stream.
     * @return the {@link Flux} of dossierTypes.
     */
    @GetMapping(value = "/dossier-types", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DossierTypeDTO> getAllDossierTypesAsStream() {
        log.debug("REST request to get all DossierTypes as a stream");
        return dossierTypeService.findAll();
    }

    /**
     * {@code GET  /dossier-types/:id} : get the "id" dossierType.
     *
     * @param id the id of the dossierTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossier-types/{id}")
    public Mono<ResponseEntity<DossierTypeDTO>> getDossierType(@PathVariable String id) {
        log.debug("REST request to get DossierType : {}", id);
        Mono<DossierTypeDTO> dossierTypeDTO = dossierTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierTypeDTO);
    }

    /**
     * {@code DELETE  /dossier-types/:id} : delete the "id" dossierType.
     *
     * @param id the id of the dossierTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossier-types/{id}")
    public Mono<ResponseEntity<Void>> deleteDossierType(@PathVariable String id) {
        log.debug("REST request to delete DossierType : {}", id);
        return dossierTypeService
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
