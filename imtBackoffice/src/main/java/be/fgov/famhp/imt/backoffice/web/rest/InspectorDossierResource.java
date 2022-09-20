package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import be.fgov.famhp.imt.backoffice.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorDossierService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectorDossier}.
 */
@RestController
@RequestMapping("/api")
public class InspectorDossierResource {

    private final Logger log = LoggerFactory.getLogger(InspectorDossierResource.class);

    private static final String ENTITY_NAME = "imtBackofficeInspectorDossier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectorDossierService inspectorDossierService;

    private final InspectorDossierRepository inspectorDossierRepository;

    public InspectorDossierResource(
        InspectorDossierService inspectorDossierService,
        InspectorDossierRepository inspectorDossierRepository
    ) {
        this.inspectorDossierService = inspectorDossierService;
        this.inspectorDossierRepository = inspectorDossierRepository;
    }

    /**
     * {@code POST  /inspector-dossiers} : Create a new inspectorDossier.
     *
     * @param inspectorDossier the inspectorDossier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectorDossier, or with status {@code 400 (Bad Request)} if the inspectorDossier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspector-dossiers")
    public ResponseEntity<InspectorDossier> createInspectorDossier(@RequestBody InspectorDossier inspectorDossier)
        throws URISyntaxException {
        log.debug("REST request to save InspectorDossier : {}", inspectorDossier);
        if (inspectorDossier.getId() != null) {
            throw new BadRequestAlertException("A new inspectorDossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspectorDossier result = inspectorDossierService.save(inspectorDossier);
        return ResponseEntity
            .created(new URI("/api/inspector-dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspector-dossiers/:id} : Updates an existing inspectorDossier.
     *
     * @param id the id of the inspectorDossier to save.
     * @param inspectorDossier the inspectorDossier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDossier,
     * or with status {@code 400 (Bad Request)} if the inspectorDossier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDossier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspector-dossiers/{id}")
    public ResponseEntity<InspectorDossier> updateInspectorDossier(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDossier inspectorDossier
    ) throws URISyntaxException {
        log.debug("REST request to update InspectorDossier : {}, {}", id, inspectorDossier);
        if (inspectorDossier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDossier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorDossierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspectorDossier result = inspectorDossierService.update(inspectorDossier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectorDossier.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspector-dossiers/:id} : Partial updates given fields of an existing inspectorDossier, field will ignore if it is null
     *
     * @param id the id of the inspectorDossier to save.
     * @param inspectorDossier the inspectorDossier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDossier,
     * or with status {@code 400 (Bad Request)} if the inspectorDossier is not valid,
     * or with status {@code 404 (Not Found)} if the inspectorDossier is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDossier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspector-dossiers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectorDossier> partialUpdateInspectorDossier(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDossier inspectorDossier
    ) throws URISyntaxException {
        log.debug("REST request to partial update InspectorDossier partially : {}, {}", id, inspectorDossier);
        if (inspectorDossier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDossier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorDossierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectorDossier> result = inspectorDossierService.partialUpdate(inspectorDossier);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectorDossier.getId())
        );
    }

    /**
     * {@code GET  /inspector-dossiers} : get all the inspectorDossiers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectorDossiers in body.
     */
    @GetMapping("/inspector-dossiers")
    public List<InspectorDossier> getAllInspectorDossiers() {
        log.debug("REST request to get all InspectorDossiers");
        return inspectorDossierService.findAll();
    }

    /**
     * {@code GET  /inspector-dossiers/:id} : get the "id" inspectorDossier.
     *
     * @param id the id of the inspectorDossier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectorDossier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspector-dossiers/{id}")
    public ResponseEntity<InspectorDossier> getInspectorDossier(@PathVariable String id) {
        log.debug("REST request to get InspectorDossier : {}", id);
        Optional<InspectorDossier> inspectorDossier = inspectorDossierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectorDossier);
    }

    /**
     * {@code DELETE  /inspector-dossiers/:id} : delete the "id" inspectorDossier.
     *
     * @param id the id of the inspectorDossier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspector-dossiers/{id}")
    public ResponseEntity<Void> deleteInspectorDossier(@PathVariable String id) {
        log.debug("REST request to delete InspectorDossier : {}", id);
        inspectorDossierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
