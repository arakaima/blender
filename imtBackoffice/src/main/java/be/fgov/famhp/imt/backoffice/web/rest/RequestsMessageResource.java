package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsMessageService;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsMessageDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.RequestsMessage}.
 */
@RestController
@RequestMapping("/api")
public class RequestsMessageResource {

    private final Logger log = LoggerFactory.getLogger(RequestsMessageResource.class);

    private static final String ENTITY_NAME = "imtBackofficeRequestsMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestsMessageService requestsMessageService;

    private final RequestsMessageRepository requestsMessageRepository;

    public RequestsMessageResource(RequestsMessageService requestsMessageService, RequestsMessageRepository requestsMessageRepository) {
        this.requestsMessageService = requestsMessageService;
        this.requestsMessageRepository = requestsMessageRepository;
    }

    /**
     * {@code POST  /requests-messages} : Create a new requestsMessage.
     *
     * @param requestsMessageDTO the requestsMessageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requestsMessageDTO, or with status {@code 400 (Bad Request)} if the requestsMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requests-messages")
    public ResponseEntity<RequestsMessageDTO> createRequestsMessage(@RequestBody RequestsMessageDTO requestsMessageDTO)
        throws URISyntaxException {
        log.debug("REST request to save RequestsMessage : {}", requestsMessageDTO);
        if (requestsMessageDTO.getId() != null) {
            throw new BadRequestAlertException("A new requestsMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RequestsMessageDTO result = requestsMessageService.save(requestsMessageDTO);
        return ResponseEntity
            .created(new URI("/api/requests-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /requests-messages/:id} : Updates an existing requestsMessage.
     *
     * @param id the id of the requestsMessageDTO to save.
     * @param requestsMessageDTO the requestsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the requestsMessageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requestsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requests-messages/{id}")
    public ResponseEntity<RequestsMessageDTO> updateRequestsMessage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestsMessageDTO requestsMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RequestsMessage : {}, {}", id, requestsMessageDTO);
        if (requestsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestsMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RequestsMessageDTO result = requestsMessageService.update(requestsMessageDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestsMessageDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /requests-messages/:id} : Partial updates given fields of an existing requestsMessage, field will ignore if it is null
     *
     * @param id the id of the requestsMessageDTO to save.
     * @param requestsMessageDTO the requestsMessageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requestsMessageDTO,
     * or with status {@code 400 (Bad Request)} if the requestsMessageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the requestsMessageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the requestsMessageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requests-messages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RequestsMessageDTO> partialUpdateRequestsMessage(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RequestsMessageDTO requestsMessageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RequestsMessage partially : {}, {}", id, requestsMessageDTO);
        if (requestsMessageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requestsMessageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestsMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RequestsMessageDTO> result = requestsMessageService.partialUpdate(requestsMessageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, requestsMessageDTO.getId())
        );
    }

    /**
     * {@code GET  /requests-messages} : get all the requestsMessages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requestsMessages in body.
     */
    @GetMapping("/requests-messages")
    public List<RequestsMessageDTO> getAllRequestsMessages() {
        log.debug("REST request to get all RequestsMessages");
        return requestsMessageService.findAll();
    }

    /**
     * {@code GET  /requests-messages/:id} : get the "id" requestsMessage.
     *
     * @param id the id of the requestsMessageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requestsMessageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requests-messages/{id}")
    public ResponseEntity<RequestsMessageDTO> getRequestsMessage(@PathVariable String id) {
        log.debug("REST request to get RequestsMessage : {}", id);
        Optional<RequestsMessageDTO> requestsMessageDTO = requestsMessageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(requestsMessageDTO);
    }

    /**
     * {@code DELETE  /requests-messages/:id} : delete the "id" requestsMessage.
     *
     * @param id the id of the requestsMessageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requests-messages/{id}")
    public ResponseEntity<Void> deleteRequestsMessage(@PathVariable String id) {
        log.debug("REST request to delete RequestsMessage : {}", id);
        requestsMessageService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
