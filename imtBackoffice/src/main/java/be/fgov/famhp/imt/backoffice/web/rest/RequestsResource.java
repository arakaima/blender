package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsService;
import be.fgov.famhp.imt.backoffice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Requests}.
 */
@RestController
@RequestMapping("/api")
public class RequestsResource {

    private final Logger log = LoggerFactory.getLogger(RequestsResource.class);

    private static final String ENTITY_NAME = "imtBackofficeRequests";

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
     * @param requests the requests to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requests, or with status {@code 400 (Bad Request)} if the requests has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests")
    public ResponseEntity<Requests> createRequests(@RequestBody Requests requests) throws URISyntaxException {
        log.debug("REST request to save Requests : {}", requests);
        if (requests.getId() != null) {
            throw new BadRequestAlertException("A new requests cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Requests result = requestsService.save(requests);
        return ResponseEntity
            .created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /requests/:id} : Updates an existing requests.
     *
     * @param id the id of the requests to save.
     * @param requests the requests to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requests,
     * or with status {@code 400 (Bad Request)} if the requests is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requests couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requests/{id}")
    public ResponseEntity<Requests> updateRequests(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Requests requests
    ) throws URISyntaxException {
        log.debug("REST request to update Requests : {}, {}", id, requests);
        if (requests.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requests.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Requests result = requestsService.update(requests);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requests.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /requests/:id} : Partial updates given fields of an existing requests, field will ignore if it is null
     *
     * @param id the id of the requests to save.
     * @param requests the requests to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requests,
     * or with status {@code 400 (Bad Request)} if the requests is not valid,
     * or with status {@code 404 (Not Found)} if the requests is not found,
     * or with status {@code 500 (Internal Server Error)} if the requests couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Requests> partialUpdateRequests(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Requests requests
    ) throws URISyntaxException {
        log.debug("REST request to partial update Requests partially : {}, {}", id, requests);
        if (requests.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requests.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Requests> result = requestsService.partialUpdate(requests);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requests.getId())
        );
    }

    /**
     * {@code GET  /requests} : get all the requests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests")
    public List<Requests> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestsService.findAll();
    }

    /**
     * {@code GET  /requests/:id} : get the "id" requests.
     *
     * @param id the id of the requests to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requests, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requests/{id}")
    public ResponseEntity<Requests> getRequests(@PathVariable String id) {
        log.debug("REST request to get Requests : {}", id);
        Optional<Requests> requests = requestsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requests);
    }

    /**
     * {@code DELETE  /requests/:id} : delete the "id" requests.
     *
     * @param id the id of the requests to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequests(@PathVariable String id) {
        log.debug("REST request to delete Requests : {}", id);
        requestsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
