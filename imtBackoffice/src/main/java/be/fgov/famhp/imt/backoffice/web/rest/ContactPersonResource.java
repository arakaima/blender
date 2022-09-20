package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import be.fgov.famhp.imt.backoffice.repository.ContactPersonRepository;
import be.fgov.famhp.imt.backoffice.service.ContactPersonService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.ContactPerson}.
 */
@RestController
@RequestMapping("/api")
public class ContactPersonResource {

    private final Logger log = LoggerFactory.getLogger(ContactPersonResource.class);

    private static final String ENTITY_NAME = "imtBackofficeContactPerson";

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
     * @param contactPerson the contactPerson to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactPerson, or with status {@code 400 (Bad Request)} if the contactPerson has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-people")
    public ResponseEntity<ContactPerson> createContactPerson(@RequestBody ContactPerson contactPerson) throws URISyntaxException {
        log.debug("REST request to save ContactPerson : {}", contactPerson);
        if (contactPerson.getId() != null) {
            throw new BadRequestAlertException("A new contactPerson cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactPerson result = contactPersonService.save(contactPerson);
        return ResponseEntity
            .created(new URI("/api/contact-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /contact-people/:id} : Updates an existing contactPerson.
     *
     * @param id the id of the contactPerson to save.
     * @param contactPerson the contactPerson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPerson,
     * or with status {@code 400 (Bad Request)} if the contactPerson is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactPerson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-people/{id}")
    public ResponseEntity<ContactPerson> updateContactPerson(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContactPerson contactPerson
    ) throws URISyntaxException {
        log.debug("REST request to update ContactPerson : {}, {}", id, contactPerson);
        if (contactPerson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPerson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContactPerson result = contactPersonService.update(contactPerson);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPerson.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /contact-people/:id} : Partial updates given fields of an existing contactPerson, field will ignore if it is null
     *
     * @param id the id of the contactPerson to save.
     * @param contactPerson the contactPerson to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactPerson,
     * or with status {@code 400 (Bad Request)} if the contactPerson is not valid,
     * or with status {@code 404 (Not Found)} if the contactPerson is not found,
     * or with status {@code 500 (Internal Server Error)} if the contactPerson couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contact-people/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContactPerson> partialUpdateContactPerson(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody ContactPerson contactPerson
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContactPerson partially : {}, {}", id, contactPerson);
        if (contactPerson.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contactPerson.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contactPersonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContactPerson> result = contactPersonService.partialUpdate(contactPerson);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactPerson.getId())
        );
    }

    /**
     * {@code GET  /contact-people} : get all the contactPeople.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactPeople in body.
     */
    @GetMapping("/contact-people")
    public List<ContactPerson> getAllContactPeople() {
        log.debug("REST request to get all ContactPeople");
        return contactPersonService.findAll();
    }

    /**
     * {@code GET  /contact-people/:id} : get the "id" contactPerson.
     *
     * @param id the id of the contactPerson to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactPerson, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-people/{id}")
    public ResponseEntity<ContactPerson> getContactPerson(@PathVariable String id) {
        log.debug("REST request to get ContactPerson : {}", id);
        Optional<ContactPerson> contactPerson = contactPersonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactPerson);
    }

    /**
     * {@code DELETE  /contact-people/:id} : delete the "id" contactPerson.
     *
     * @param id the id of the contactPerson to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-people/{id}")
    public ResponseEntity<Void> deleteContactPerson(@PathVariable String id) {
        log.debug("REST request to delete ContactPerson : {}", id);
        contactPersonService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
