package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.DossierStatusRepository;
import be.fgov.famhp.imt.backoffice.service.DossierStatusService;
import be.fgov.famhp.imt.backoffice.service.dto.DossierStatusDTO;
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
     * @param dossierStatusDTO the dossierStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierStatusDTO, or with status {@code 400 (Bad Request)} if the dossierStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossier-statuses")
    public ResponseEntity<DossierStatusDTO> createDossierStatus(@RequestBody DossierStatusDTO dossierStatusDTO) throws URISyntaxException {
        log.debug("REST request to save DossierStatus : {}", dossierStatusDTO);
        if (dossierStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossierStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierStatusDTO result = dossierStatusService.save(dossierStatusDTO);
        return ResponseEntity
            .created(new URI("/api/dossier-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dossier-statuses/:id} : Updates an existing dossierStatus.
     *
     * @param id the id of the dossierStatusDTO to save.
     * @param dossierStatusDTO the dossierStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierStatusDTO,
     * or with status {@code 400 (Bad Request)} if the dossierStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossier-statuses/{id}")
    public ResponseEntity<DossierStatusDTO> updateDossierStatus(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierStatusDTO dossierStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DossierStatus : {}, {}", id, dossierStatusDTO);
        if (dossierStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DossierStatusDTO result = dossierStatusService.update(dossierStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierStatusDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dossier-statuses/:id} : Partial updates given fields of an existing dossierStatus, field will ignore if it is null
     *
     * @param id the id of the dossierStatusDTO to save.
     * @param dossierStatusDTO the dossierStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierStatusDTO,
     * or with status {@code 400 (Bad Request)} if the dossierStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dossierStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossier-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DossierStatusDTO> partialUpdateDossierStatus(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierStatusDTO dossierStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DossierStatus partially : {}, {}", id, dossierStatusDTO);
        if (dossierStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DossierStatusDTO> result = dossierStatusService.partialUpdate(dossierStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierStatusDTO.getId())
        );
    }

    /**
     * {@code GET  /dossier-statuses} : get all the dossierStatuses.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierStatuses in body.
     */
    @GetMapping("/dossier-statuses")
    public List<DossierStatusDTO> getAllDossierStatuses() {
        log.debug("REST request to get all DossierStatuses");
        return dossierStatusService.findAll();
    }

    /**
     * {@code GET  /dossier-statuses/:id} : get the "id" dossierStatus.
     *
     * @param id the id of the dossierStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossier-statuses/{id}")
    public ResponseEntity<DossierStatusDTO> getDossierStatus(@PathVariable String id) {
        log.debug("REST request to get DossierStatus : {}", id);
        Optional<DossierStatusDTO> dossierStatusDTO = dossierStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierStatusDTO);
    }

    /**
     * {@code DELETE  /dossier-statuses/:id} : delete the "id" dossierStatus.
     *
     * @param id the id of the dossierStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossier-statuses/{id}")
    public ResponseEntity<Void> deleteDossierStatus(@PathVariable String id) {
        log.debug("REST request to delete DossierStatus : {}", id);
        dossierStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
