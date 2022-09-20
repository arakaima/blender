package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.RequestsRepository;
import be.fgov.famhp.imt.gateway.service.RequestsService;
import be.fgov.famhp.imt.gateway.service.dto.RequestsDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.Requests}.
 */
@RestController
@RequestMapping("/api")
public class RequestsResource {

    private final Logger log = LoggerFactory.getLogger(RequestsResource.class);

    private static final String ENTITY_NAME = "requests";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestsService requestsService;

    private final RequestsRepository requestsRepository;

    public RequestsResource(RequestsService requestsService, RequestsRepository requestsRepository) {
        this.requestsService = requestsService;
        this.requestsRepository = requestsRepository;
    }

    /**
     * {@code POST  /requests} : Create a new requests.
     *
     * @param requestsDTO the requestsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestsDTO, or with status {@code 400 (Bad Request)} if the requests has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests")
    public Mono<ResponseEntity<RequestsDTO>> createRequests(@RequestBody RequestsDTO requestsDTO) throws URISyntaxException {
        log.debug("REST request to save Requests : {}", requestsDTO);
        if (requestsDTO.getId() != null) {
            throw new BadRequestAlertException("A new requests cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return requestsService
            .save(requestsDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/requests/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /requests/:id} : Updates an existing requests.
     *
     * @param id the id of the requestsDTO to save.
     * @param requestsDTO the requestsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestsDTO,
     * or with status {@code 400 (Bad Request)} if the requestsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requests/{id}")
    public Mono<ResponseEntity<RequestsDTO>> updateRequests(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestsDTO requestsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Requests : {}, {}", id, requestsDTO);
        if (requestsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return requestsService
                    .update(requestsDTO)
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
     * {@code PATCH  /requests/:id} : Partial updates given fields of an existing requests, field will ignore if it is null
     *
     * @param id the id of the requestsDTO to save.
     * @param requestsDTO the requestsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestsDTO,
     * or with status {@code 400 (Bad Request)} if the requestsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<RequestsDTO>> partialUpdateRequests(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestsDTO requestsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Requests partially : {}, {}", id, requestsDTO);
        if (requestsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return requestsRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<RequestsDTO> result = requestsService.partialUpdate(requestsDTO);

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
     * {@code GET  /requests} : get all the requests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests")
    public Mono<List<RequestsDTO>> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestsService.findAll().collectList();
    }

    /**
     * {@code GET  /requests} : get all the requests as a stream.
     * @return the {@link Flux} of requests.
     */
    @GetMapping(value = "/requests", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<RequestsDTO> getAllRequestsAsStream() {
        log.debug("REST request to get all Requests as a stream");
        return requestsService.findAll();
    }

    /**
     * {@code GET  /requests/:id} : get the "id" requests.
     *
     * @param id the id of the requestsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requests/{id}")
    public Mono<ResponseEntity<RequestsDTO>> getRequests(@PathVariable String id) {
        log.debug("REST request to get Requests : {}", id);
        Mono<RequestsDTO> requestsDTO = requestsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestsDTO);
    }

    /**
     * {@code DELETE  /requests/:id} : delete the "id" requests.
     *
     * @param id the id of the requestsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requests/{id}")
    public Mono<ResponseEntity<Void>> deleteRequests(@PathVariable String id) {
        log.debug("REST request to delete Requests : {}", id);
        return requestsService
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
