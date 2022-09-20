package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.ExpertRepository;
import be.fgov.famhp.imt.gateway.service.ExpertService;
import be.fgov.famhp.imt.gateway.service.dto.ExpertDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.Expert}.
 */
@RestController
@RequestMapping("/api")
public class ExpertResource {

    private final Logger log = LoggerFactory.getLogger(ExpertResource.class);

    private static final String ENTITY_NAME = "expert";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExpertService expertService;

    private final ExpertRepository expertRepository;

    public ExpertResource(ExpertService expertService, ExpertRepository expertRepository) {
        this.expertService = expertService;
        this.expertRepository = expertRepository;
    }

    /**
     * {@code POST  /experts} : Create a new expert.
     *
     * @param expertDTO the expertDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expertDTO, or with status {@code 400 (Bad Request)} if the expert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experts")
    public Mono<ResponseEntity<ExpertDTO>> createExpert(@RequestBody ExpertDTO expertDTO) throws URISyntaxException {
        log.debug("REST request to save Expert : {}", expertDTO);
        if (expertDTO.getId() != null) {
            throw new BadRequestAlertException("A new expert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return expertService
            .save(expertDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/experts/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /experts/:id} : Updates an existing expert.
     *
     * @param id the id of the expertDTO to save.
     * @param expertDTO the expertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertDTO,
     * or with status {@code 400 (Bad Request)} if the expertDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experts/{id}")
    public Mono<ResponseEntity<ExpertDTO>> updateExpert(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ExpertDTO expertDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Expert : {}, {}", id, expertDTO);
        if (expertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return expertRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return expertService
                    .update(expertDTO)
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
     * {@code PATCH  /experts/:id} : Partial updates given fields of an existing expert, field will ignore if it is null
     *
     * @param id the id of the expertDTO to save.
     * @param expertDTO the expertDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expertDTO,
     * or with status {@code 400 (Bad Request)} if the expertDTO is not valid,
     * or with status {@code 404 (Not Found)} if the expertDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the expertDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/experts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ExpertDTO>> partialUpdateExpert(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ExpertDTO expertDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Expert partially : {}, {}", id, expertDTO);
        if (expertDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expertDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return expertRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ExpertDTO> result = expertService.partialUpdate(expertDTO);

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
     * {@code GET  /experts} : get all the experts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experts in body.
     */
    @GetMapping("/experts")
    public Mono<List<ExpertDTO>> getAllExperts() {
        log.debug("REST request to get all Experts");
        return expertService.findAll().collectList();
    }

    /**
     * {@code GET  /experts} : get all the experts as a stream.
     * @return the {@link Flux} of experts.
     */
    @GetMapping(value = "/experts", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ExpertDTO> getAllExpertsAsStream() {
        log.debug("REST request to get all Experts as a stream");
        return expertService.findAll();
    }

    /**
     * {@code GET  /experts/:id} : get the "id" expert.
     *
     * @param id the id of the expertDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expertDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experts/{id}")
    public Mono<ResponseEntity<ExpertDTO>> getExpert(@PathVariable String id) {
        log.debug("REST request to get Expert : {}", id);
        Mono<ExpertDTO> expertDTO = expertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expertDTO);
    }

    /**
     * {@code DELETE  /experts/:id} : delete the "id" expert.
     *
     * @param id the id of the expertDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experts/{id}")
    public Mono<ResponseEntity<Void>> deleteExpert(@PathVariable String id) {
        log.debug("REST request to delete Expert : {}", id);
        return expertService
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
