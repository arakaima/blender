package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.InspectionReportRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionReportService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionReportDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectionReport}.
 */
@RestController
@RequestMapping("/api")
public class InspectionReportResource {

    private final Logger log = LoggerFactory.getLogger(InspectionReportResource.class);

    private static final String ENTITY_NAME = "imtBackofficeInspectionReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectionReportService inspectionReportService;

    private final InspectionReportRepository inspectionReportRepository;

    public InspectionReportResource(
        InspectionReportService inspectionReportService,
        InspectionReportRepository inspectionReportRepository
    ) {
        this.inspectionReportService = inspectionReportService;
        this.inspectionReportRepository = inspectionReportRepository;
    }

    /**
     * {@code POST  /inspection-reports} : Create a new inspectionReport.
     *
     * @param inspectionReportDTO the inspectionReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectionReportDTO, or with status {@code 400 (Bad Request)} if the inspectionReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspection-reports")
    public ResponseEntity<InspectionReportDTO> createInspectionReport(@RequestBody InspectionReportDTO inspectionReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save InspectionReport : {}", inspectionReportDTO);
        if (inspectionReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspectionReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspectionReportDTO result = inspectionReportService.save(inspectionReportDTO);
        return ResponseEntity
            .created(new URI("/api/inspection-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspection-reports/:id} : Updates an existing inspectionReport.
     *
     * @param id the id of the inspectionReportDTO to save.
     * @param inspectionReportDTO the inspectionReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionReportDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectionReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspection-reports/{id}")
    public ResponseEntity<InspectionReportDTO> updateInspectionReport(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionReportDTO inspectionReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InspectionReport : {}, {}", id, inspectionReportDTO);
        if (inspectionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspectionReportDTO result = inspectionReportService.update(inspectionReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionReportDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspection-reports/:id} : Partial updates given fields of an existing inspectionReport, field will ignore if it is null
     *
     * @param id the id of the inspectionReportDTO to save.
     * @param inspectionReportDTO the inspectionReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionReportDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectionReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectionReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspection-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectionReportDTO> partialUpdateInspectionReport(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionReportDTO inspectionReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InspectionReport partially : {}, {}", id, inspectionReportDTO);
        if (inspectionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectionReportDTO> result = inspectionReportService.partialUpdate(inspectionReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionReportDTO.getId())
        );
    }

    /**
     * {@code GET  /inspection-reports} : get all the inspectionReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectionReports in body.
     */
    @GetMapping("/inspection-reports")
    public List<InspectionReportDTO> getAllInspectionReports() {
        log.debug("REST request to get all InspectionReports");
        return inspectionReportService.findAll();
    }

    /**
     * {@code GET  /inspection-reports/:id} : get the "id" inspectionReport.
     *
     * @param id the id of the inspectionReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectionReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspection-reports/{id}")
    public ResponseEntity<InspectionReportDTO> getInspectionReport(@PathVariable String id) {
        log.debug("REST request to get InspectionReport : {}", id);
        Optional<InspectionReportDTO> inspectionReportDTO = inspectionReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectionReportDTO);
    }

    /**
     * {@code DELETE  /inspection-reports/:id} : delete the "id" inspectionReport.
     *
     * @param id the id of the inspectionReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspection-reports/{id}")
    public ResponseEntity<Void> deleteInspectionReport(@PathVariable String id) {
        log.debug("REST request to delete InspectionReport : {}", id);
        inspectionReportService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
