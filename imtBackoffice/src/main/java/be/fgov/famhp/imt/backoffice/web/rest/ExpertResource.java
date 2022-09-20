package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.Expert;
import be.fgov.famhp.imt.backoffice.repository.ExpertRepository;
import be.fgov.famhp.imt.backoffice.service.ExpertService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Expert}.
 */
@RestController
@RequestMapping("/api")
public class ExpertResource {

    private final Logger log = LoggerFactory.getLogger(ExpertResource.class);

    private static final String ENTITY_NAME = "imtBackofficeExpert";

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
     * @param expert the expert to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new expert, or with status {@code 400 (Bad Request)} if the expert has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/experts")
    public ResponseEntity<Expert> createExpert(@RequestBody Expert expert) throws URISyntaxException {
        log.debug("REST request to save Expert : {}", expert);
        if (expert.getId() != null) {
            throw new BadRequestAlertException("A new expert cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Expert result = expertService.save(expert);
        return ResponseEntity
            .created(new URI("/api/experts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /experts/:id} : Updates an existing expert.
     *
     * @param id the id of the expert to save.
     * @param expert the expert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expert,
     * or with status {@code 400 (Bad Request)} if the expert is not valid,
     * or with status {@code 500 (Internal Server Error)} if the expert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/experts/{id}")
    public ResponseEntity<Expert> updateExpert(@PathVariable(value = "id", required = false) final String id, @RequestBody Expert expert)
        throws URISyntaxException {
        log.debug("REST request to update Expert : {}, {}", id, expert);
        if (expert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expert.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Expert result = expertService.update(expert);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expert.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /experts/:id} : Partial updates given fields of an existing expert, field will ignore if it is null
     *
     * @param id the id of the expert to save.
     * @param expert the expert to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated expert,
     * or with status {@code 400 (Bad Request)} if the expert is not valid,
     * or with status {@code 404 (Not Found)} if the expert is not found,
     * or with status {@code 500 (Internal Server Error)} if the expert couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/experts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Expert> partialUpdateExpert(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Expert expert
    ) throws URISyntaxException {
        log.debug("REST request to partial update Expert partially : {}, {}", id, expert);
        if (expert.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, expert.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!expertRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Expert> result = expertService.partialUpdate(expert);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, expert.getId()));
    }

    /**
     * {@code GET  /experts} : get all the experts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of experts in body.
     */
    @GetMapping("/experts")
    public List<Expert> getAllExperts() {
        log.debug("REST request to get all Experts");
        return expertService.findAll();
    }

    /**
     * {@code GET  /experts/:id} : get the "id" expert.
     *
     * @param id the id of the expert to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the expert, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/experts/{id}")
    public ResponseEntity<Expert> getExpert(@PathVariable String id) {
        log.debug("REST request to get Expert : {}", id);
        Optional<Expert> expert = expertService.findOne(id);
        return ResponseUtil.wrapOrNotFound(expert);
    }

    /**
     * {@code DELETE  /experts/:id} : delete the "id" expert.
     *
     * @param id the id of the expert to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/experts/{id}")
    public ResponseEntity<Void> deleteExpert(@PathVariable String id) {
        log.debug("REST request to delete Expert : {}", id);
        expertService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
