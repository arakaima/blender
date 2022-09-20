package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.Inspector;
import be.fgov.famhp.imt.backoffice.repository.InspectorRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Inspector}.
 */
@RestController
@RequestMapping("/api")
public class InspectorResource {

    private final Logger log = LoggerFactory.getLogger(InspectorResource.class);

    private static final String ENTITY_NAME = "imtBackofficeInspector";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectorService inspectorService;

    private final InspectorRepository inspectorRepository;

    public InspectorResource(InspectorService inspectorService, InspectorRepository inspectorRepository) {
        this.inspectorService = inspectorService;
        this.inspectorRepository = inspectorRepository;
    }

    /**
     * {@code POST  /inspectors} : Create a new inspector.
     *
     * @param inspector the inspector to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspector, or with status {@code 400 (Bad Request)} if the inspector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspectors")
    public ResponseEntity<Inspector> createInspector(@RequestBody Inspector inspector) throws URISyntaxException {
        log.debug("REST request to save Inspector : {}", inspector);
        if (inspector.getId() != null) {
            throw new BadRequestAlertException("A new inspector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inspector result = inspectorService.save(inspector);
        return ResponseEntity
            .created(new URI("/api/inspectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspectors/:id} : Updates an existing inspector.
     *
     * @param id the id of the inspector to save.
     * @param inspector the inspector to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspector,
     * or with status {@code 400 (Bad Request)} if the inspector is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspector couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspectors/{id}")
    public ResponseEntity<Inspector> updateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Inspector inspector
    ) throws URISyntaxException {
        log.debug("REST request to update Inspector : {}, {}", id, inspector);
        if (inspector.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspector.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Inspector result = inspectorService.update(inspector);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspector.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspectors/:id} : Partial updates given fields of an existing inspector, field will ignore if it is null
     *
     * @param id the id of the inspector to save.
     * @param inspector the inspector to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspector,
     * or with status {@code 400 (Bad Request)} if the inspector is not valid,
     * or with status {@code 404 (Not Found)} if the inspector is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspector couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspectors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Inspector> partialUpdateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody Inspector inspector
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspector partially : {}, {}", id, inspector);
        if (inspector.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspector.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Inspector> result = inspectorService.partialUpdate(inspector);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspector.getId())
        );
    }

    /**
     * {@code GET  /inspectors} : get all the inspectors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectors in body.
     */
    @GetMapping("/inspectors")
    public List<Inspector> getAllInspectors() {
        log.debug("REST request to get all Inspectors");
        return inspectorService.findAll();
    }

    /**
     * {@code GET  /inspectors/:id} : get the "id" inspector.
     *
     * @param id the id of the inspector to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspector, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspectors/{id}")
    public ResponseEntity<Inspector> getInspector(@PathVariable String id) {
        log.debug("REST request to get Inspector : {}", id);
        Optional<Inspector> inspector = inspectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspector);
    }

    /**
     * {@code DELETE  /inspectors/:id} : delete the "id" inspector.
     *
     * @param id the id of the inspector to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspectors/{id}")
    public ResponseEntity<Void> deleteInspector(@PathVariable String id) {
        log.debug("REST request to delete Inspector : {}", id);
        inspectorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
