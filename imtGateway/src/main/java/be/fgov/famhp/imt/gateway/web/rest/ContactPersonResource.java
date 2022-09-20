package be.fgov.famhp.imt.gateway.web.rest;

import be.fgov.famhp.imt.gateway.repository.ContactPersonRepository;
import be.fgov.famhp.imt.gateway.service.ContactPersonService;
import be.fgov.famhp.imt.gateway.service.dto.ContactPersonDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.gateway.domain.ContactPerson}.
 */
@RestController
@RequestMapping("/api")
public class ContactPersonResource {

    private final Logger log = LoggerFactory.getLogger(ContactPersonResource.class);

    private static final String ENTITY_NAME = "contactPerson";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactPersonService contactPersonService;

    private final ContactPersonRepository contactPersonRepository;

    public ContactPersonResource(ContactPersonService contactPersonService, ContactPersonRepository contactPersonRepository) {
        this.contactPersonService = contactPersonService;
        this.contactPersonRepository = contactPersonRepository;
    }

    /**
     * {@code POST  /contact-people} : Create a new contactPerson.
     *
     * @param contactPersonDTO the contactPersonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactPersonDTO, or with status {@code 400 (Bad Request)} if the contactPerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-people")
    public Mono<ResponseEntity<ContactPersonDTO>> createContactPerson(@RequestBody ContactPersonDTO contactPersonDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContactPerson : {}", contactPersonDTO);
        if (contactPersonDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return contactPersonService
            .save(contactPersonDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/contact-people/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /contact-people/:id} : Updates an existing contactPerson.
     *
     * @param id the id of the contactPersonDTO to save.
     * @param contactPersonDTO the contactPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPersonDTO,
     * or with status {@code 400 (Bad Request)} if the contactPersonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-people/{id}")
    public Mono<ResponseEntity<ContactPersonDTO>> updateContactPerson(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContactPersonDTO contactPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContactPerson : {}, {}", id, contactPersonDTO);
        if (contactPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contactPersonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return contactPersonService
                    .update(contactPersonDTO)
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
     * {@code PATCH  /contact-people/:id} : Partial updates given fields of an existing contactPerson, field will ignore if it is null
     *
     * @param id the id of the contactPersonDTO to save.
     * @param contactPersonDTO the contactPersonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPersonDTO,
     * or with status {@code 400 (Bad Request)} if the contactPersonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contactPersonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactPersonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-people/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ContactPersonDTO>> partialUpdateContactPerson(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContactPersonDTO contactPersonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactPerson partially : {}, {}", id, contactPersonDTO);
        if (contactPersonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPersonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return contactPersonRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ContactPersonDTO> result = contactPersonService.partialUpdate(contactPersonDTO);

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
     * {@code GET  /contact-people} : get all the contactPeople.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactPeople in body.
     */
    @GetMapping("/contact-people")
    public Mono<List<ContactPersonDTO>> getAllContactPeople() {
        log.debug("REST request to get all ContactPeople");
        return contactPersonService.findAll().collectList();
    }

    /**
     * {@code GET  /contact-people} : get all the contactPeople as a stream.
     * @return the {@link Flux} of contactPeople.
     */
    @GetMapping(value = "/contact-people", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ContactPersonDTO> getAllContactPeopleAsStream() {
        log.debug("REST request to get all ContactPeople as a stream");
        return contactPersonService.findAll();
    }

    /**
     * {@code GET  /contact-people/:id} : get the "id" contactPerson.
     *
     * @param id the id of the contactPersonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactPersonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-people/{id}")
    public Mono<ResponseEntity<ContactPersonDTO>> getContactPerson(@PathVariable String id) {
        log.debug("REST request to get ContactPerson : {}", id);
        Mono<ContactPersonDTO> contactPersonDTO = contactPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactPersonDTO);
    }

    /**
     * {@code DELETE  /contact-people/:id} : delete the "id" contactPerson.
     *
     * @param id the id of the contactPersonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-people/{id}")
    public Mono<ResponseEntity<Void>> deleteContactPerson(@PathVariable String id) {
        log.debug("REST request to delete ContactPerson : {}", id);
        return contactPersonService
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
