package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsService;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsDTO;
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
     * @param requestsDTO the requestsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestsDTO, or with status {@code 400 (Bad Request)} if the requests has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests")
    public ResponseEntity<RequestsDTO> createRequests(@RequestBody RequestsDTO requestsDTO) throws URISyntaxException {
        log.debug("REST request to save Requests : {}", requestsDTO);
        if (requestsDTO.getId() != null) {
            throw new BadRequestAlertException("A new requests cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestsDTO result = requestsService.save(requestsDTO);
        return ResponseEntity
            .created(new URI("/api/requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
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
    public ResponseEntity<RequestsDTO> updateRequests(
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

        if (!requestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RequestsDTO result = requestsService.update(requestsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestsDTO.getId()))
            .body(result);
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
    public ResponseEntity<RequestsDTO> partialUpdateRequests(
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

        if (!requestsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequestsDTO> result = requestsService.partialUpdate(requestsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestsDTO.getId())
        );
    }

    /**
     * {@code GET  /requests} : get all the requests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("/requests")
    public List<RequestsDTO> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestsService.findAll();
    }

    /**
     * {@code GET  /requests/:id} : get the "id" requests.
     *
     * @param id the id of the requestsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requests/{id}")
    public ResponseEntity<RequestsDTO> getRequests(@PathVariable String id) {
        log.debug("REST request to get Requests : {}", id);
        Optional<RequestsDTO> requestsDTO = requestsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestsDTO);
    }

    /**
     * {@code DELETE  /requests/:id} : delete the "id" requests.
     *
     * @param id the id of the requestsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requests/{id}")
    public ResponseEntity<Void> deleteRequests(@PathVariable String id) {
        log.debug("REST request to delete Requests : {}", id);
        requestsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
