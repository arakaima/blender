package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.CapaRepository;
import be.fgov.famhp.imt.gateway.service.CapaService;
import be.fgov.famhp.imt.gateway.service.dto.CapaDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.Capa}.
 */
@RestController
@RequestMapping("/api")
public class CapaResource {

    private final Logger log = LoggerFactory.getLogger(CapaResource.class);

    private static final String ENTITY_NAME = "capa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapaService capaService;

    private final CapaRepository capaRepository;

    public CapaResource(CapaService capaService, CapaRepository capaRepository) {
        this.capaService = capaService;
        this.capaRepository = capaRepository;
    }

    /**
     * {@code POST  /capas} : Create a new capa.
     *
     * @param capaDTO the capaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capaDTO, or with status {@code 400 (Bad Request)} if the capa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capas")
    public Mono<ResponseEntity<CapaDTO>> createCapa(@RequestBody CapaDTO capaDTO) throws URISyntaxException {
        log.debug("REST request to save Capa : {}", capaDTO);
        if (capaDTO.getId() != null) {
            throw new BadRequestAlertException("A new capa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return capaService
            .save(capaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/capas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /capas/:id} : Updates an existing capa.
     *
     * @param id the id of the capaDTO to save.
     * @param capaDTO the capaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDTO,
     * or with status {@code 400 (Bad Request)} if the capaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capas/{id}")
    public Mono<ResponseEntity<CapaDTO>> updateCapa(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDTO capaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Capa : {}, {}", id, capaDTO);
        if (capaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return capaService
                    .update(capaDTO)
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
     * {@code PATCH  /capas/:id} : Partial updates given fields of an existing capa, field will ignore if it is null
     *
     * @param id the id of the capaDTO to save.
     * @param capaDTO the capaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDTO,
     * or with status {@code 400 (Bad Request)} if the capaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the capaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the capaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CapaDTO>> partialUpdateCapa(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDTO capaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Capa partially : {}, {}", id, capaDTO);
        if (capaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return capaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CapaDTO> result = capaService.partialUpdate(capaDTO);

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
     * {@code GET  /capas} : get all the capas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capas in body.
     */
    @GetMapping("/capas")
    public Mono<List<CapaDTO>> getAllCapas() {
        log.debug("REST request to get all Capas");
        return capaService.findAll().collectList();
    }

    /**
     * {@code GET  /capas} : get all the capas as a stream.
     * @return the {@link Flux} of capas.
     */
    @GetMapping(value = "/capas", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CapaDTO> getAllCapasAsStream() {
        log.debug("REST request to get all Capas as a stream");
        return capaService.findAll();
    }

    /**
     * {@code GET  /capas/:id} : get the "id" capa.
     *
     * @param id the id of the capaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capas/{id}")
    public Mono<ResponseEntity<CapaDTO>> getCapa(@PathVariable String id) {
        log.debug("REST request to get Capa : {}", id);
        Mono<CapaDTO> capaDTO = capaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capaDTO);
    }

    /**
     * {@code DELETE  /capas/:id} : delete the "id" capa.
     *
     * @param id the id of the capaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capas/{id}")
    public Mono<ResponseEntity<Void>> deleteCapa(@PathVariable String id) {
        log.debug("REST request to delete Capa : {}", id);
        return capaService
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
