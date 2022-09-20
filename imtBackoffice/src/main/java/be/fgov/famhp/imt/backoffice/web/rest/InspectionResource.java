package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.InspectionRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Inspection}.
 */
@RestController
@RequestMapping("/api")
public class InspectionResource {

    private final Logger log = LoggerFactory.getLogger(InspectionResource.class);

    private static final String ENTITY_NAME = "imtBackofficeInspection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectionService inspectionService;

    private final InspectionRepository inspectionRepository;

    public InspectionResource(InspectionService inspectionService, InspectionRepository inspectionRepository) {
        this.inspectionService = inspectionService;
        this.inspectionRepository = inspectionRepository;
    }

    /**
     * {@code POST  /inspections} : Create a new inspection.
     *
     * @param inspectionDTO the inspectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectionDTO, or with status {@code 400 (Bad Request)} if the inspection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspections")
    public ResponseEntity<InspectionDTO> createInspection(@RequestBody InspectionDTO inspectionDTO) throws URISyntaxException {
        log.debug("REST request to save Inspection : {}", inspectionDTO);
        if (inspectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspectionDTO result = inspectionService.save(inspectionDTO);
        return ResponseEntity
            .created(new URI("/api/inspections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspections/:id} : Updates an existing inspection.
     *
     * @param id the id of the inspectionDTO to save.
     * @param inspectionDTO the inspectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspections/{id}")
    public ResponseEntity<InspectionDTO> updateInspection(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionDTO inspectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Inspection : {}, {}", id, inspectionDTO);
        if (inspectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspectionDTO result = inspectionService.update(inspectionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspections/:id} : Partial updates given fields of an existing inspection, field will ignore if it is null
     *
     * @param id the id of the inspectionDTO to save.
     * @param inspectionDTO the inspectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectionDTO> partialUpdateInspection(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionDTO inspectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspection partially : {}, {}", id, inspectionDTO);
        if (inspectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectionDTO> result = inspectionService.partialUpdate(inspectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionDTO.getId())
        );
    }

    /**
     * {@code GET  /inspections} : get all the inspections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspections in body.
     */
    @GetMapping("/inspections")
    public List<InspectionDTO> getAllInspections() {
        log.debug("REST request to get all Inspections");
        return inspectionService.findAll();
    }

    /**
     * {@code GET  /inspections/:id} : get the "id" inspection.
     *
     * @param id the id of the inspectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspections/{id}")
    public ResponseEntity<InspectionDTO> getInspection(@PathVariable String id) {
        log.debug("REST request to get Inspection : {}", id);
        Optional<InspectionDTO> inspectionDTO = inspectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectionDTO);
    }

    /**
     * {@code DELETE  /inspections/:id} : delete the "id" inspection.
     *
     * @param id the id of the inspectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspections/{id}")
    public ResponseEntity<Void> deleteInspection(@PathVariable String id) {
        log.debug("REST request to delete Inspection : {}", id);
        inspectionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
