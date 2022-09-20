package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import be.fgov.famhp.imt.backoffice.repository.DossierStatusRepository;
import be.fgov.famhp.imt.backoffice.service.DossierStatusService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.DossierStatus}.
 */
@RestController
@RequestMapping("/api")
public class DossierStatusResource {

    private final Logger log = LoggerFactory.getLogger(DossierStatusResource.class);

    private static final String ENTITY_NAME = "imtBackofficeDossierStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierStatusService dossierStatusService;

    private final DossierStatusRepository dossierStatusRepository;

    public DossierStatusResource(DossierStatusService dossierStatusService, DossierStatusRepository dossierStatusRepository) {
        this.dossierStatusService = dossierStatusService;
        this.dossierStatusRepository = dossierStatusRepository;
    }

    /**
     * {@code POST  /dossier-statuses} : Create a new dossierStatus.
     *
     * @param dossierStatus the dossierStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierStatus, or with status {@code 400 (Bad Request)} if the dossierStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossier-statuses")
    public ResponseEntity<DossierStatus> createDossierStatus(@RequestBody DossierStatus dossierStatus) throws URISyntaxException {
        log.debug("REST request to save DossierStatus : {}", dossierStatus);
        if (dossierStatus.getId() != null) {
            throw new BadRequestAlertException("A new dossierStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierStatus result = dossierStatusService.save(dossierStatus);
        return ResponseEntity
            .created(new URI("/api/dossier-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dossier-statuses/:id} : Updates an existing dossierStatus.
     *
     * @param id the id of the dossierStatus to save.
     * @param dossierStatus the dossierStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierStatus,
     * or with status {@code 400 (Bad Request)} if the dossierStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossier-statuses/{id}")
    public ResponseEntity<DossierStatus> updateDossierStatus(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierStatus dossierStatus
    ) throws URISyntaxException {
        log.debug("REST request to update DossierStatus : {}, {}", id, dossierStatus);
        if (dossierStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DossierStatus result = dossierStatusService.update(dossierStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierStatus.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dossier-statuses/:id} : Partial updates given fields of an existing dossierStatus, field will ignore if it is null
     *
     * @param id the id of the dossierStatus to save.
     * @param dossierStatus the dossierStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierStatus,
     * or with status {@code 400 (Bad Request)} if the dossierStatus is not valid,
     * or with status {@code 404 (Not Found)} if the dossierStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossier-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DossierStatus> partialUpdateDossierStatus(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierStatus dossierStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update DossierStatus partially : {}, {}", id, dossierStatus);
        if (dossierStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DossierStatus> result = dossierStatusService.partialUpdate(dossierStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierStatus.getId())
        );
    }

    /**
     * {@code GET  /dossier-statuses} : get all the dossierStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierStatuses in body.
     */
    @GetMapping("/dossier-statuses")
    public List<DossierStatus> getAllDossierStatuses() {
        log.debug("REST request to get all DossierStatuses");
        return dossierStatusService.findAll();
    }

    /**
     * {@code GET  /dossier-statuses/:id} : get the "id" dossierStatus.
     *
     * @param id the id of the dossierStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossier-statuses/{id}")
    public ResponseEntity<DossierStatus> getDossierStatus(@PathVariable String id) {
        log.debug("REST request to get DossierStatus : {}", id);
        Optional<DossierStatus> dossierStatus = dossierStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierStatus);
    }

    /**
     * {@code DELETE  /dossier-statuses/:id} : delete the "id" dossierStatus.
     *
     * @param id the id of the dossierStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossier-statuses/{id}")
    public ResponseEntity<Void> deleteDossierStatus(@PathVariable String id) {
        log.debug("REST request to delete DossierStatus : {}", id);
        dossierStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
