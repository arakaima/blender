package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.RiskAssessmentRepository;
import be.fgov.famhp.imt.backoffice.service.RiskAssessmentService;
import be.fgov.famhp.imt.backoffice.service.dto.RiskAssessmentDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.RiskAssessment}.
 */
@RestController
@RequestMapping("/api")
public class RiskAssessmentResource {

    private final Logger log = LoggerFactory.getLogger(RiskAssessmentResource.class);

    private static final String ENTITY_NAME = "imtBackofficeRiskAssessment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RiskAssessmentService riskAssessmentService;

    private final RiskAssessmentRepository riskAssessmentRepository;

    public RiskAssessmentResource(RiskAssessmentService riskAssessmentService, RiskAssessmentRepository riskAssessmentRepository) {
        this.riskAssessmentService = riskAssessmentService;
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    /**
     * {@code POST  /risk-assessments} : Create a new riskAssessment.
     *
     * @param riskAssessmentDTO the riskAssessmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new riskAssessmentDTO, or with status {@code 400 (Bad Request)} if the riskAssessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risk-assessments")
    public ResponseEntity<RiskAssessmentDTO> createRiskAssessment(@RequestBody RiskAssessmentDTO riskAssessmentDTO)
        throws URISyntaxException {
        log.debug("REST request to save RiskAssessment : {}", riskAssessmentDTO);
        if (riskAssessmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new riskAssessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RiskAssessmentDTO result = riskAssessmentService.save(riskAssessmentDTO);
        return ResponseEntity
            .created(new URI("/api/risk-assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /risk-assessments/:id} : Updates an existing riskAssessment.
     *
     * @param id the id of the riskAssessmentDTO to save.
     * @param riskAssessmentDTO the riskAssessmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated riskAssessmentDTO,
     * or with status {@code 400 (Bad Request)} if the riskAssessmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the riskAssessmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risk-assessments/{id}")
    public ResponseEntity<RiskAssessmentDTO> updateRiskAssessment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RiskAssessmentDTO riskAssessmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RiskAssessment : {}, {}", id, riskAssessmentDTO);
        if (riskAssessmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, riskAssessmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!riskAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RiskAssessmentDTO result = riskAssessmentService.update(riskAssessmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, riskAssessmentDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /risk-assessments/:id} : Partial updates given fields of an existing riskAssessment, field will ignore if it is null
     *
     * @param id the id of the riskAssessmentDTO to save.
     * @param riskAssessmentDTO the riskAssessmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated riskAssessmentDTO,
     * or with status {@code 400 (Bad Request)} if the riskAssessmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the riskAssessmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the riskAssessmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/risk-assessments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RiskAssessmentDTO> partialUpdateRiskAssessment(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody RiskAssessmentDTO riskAssessmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RiskAssessment partially : {}, {}", id, riskAssessmentDTO);
        if (riskAssessmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, riskAssessmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!riskAssessmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RiskAssessmentDTO> result = riskAssessmentService.partialUpdate(riskAssessmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, riskAssessmentDTO.getId())
        );
    }

    /**
     * {@code GET  /risk-assessments} : get all the riskAssessments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of riskAssessments in body.
     */
    @GetMapping("/risk-assessments")
    public List<RiskAssessmentDTO> getAllRiskAssessments() {
        log.debug("REST request to get all RiskAssessments");
        return riskAssessmentService.findAll();
    }

    /**
     * {@code GET  /risk-assessments/:id} : get the "id" riskAssessment.
     *
     * @param id the id of the riskAssessmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the riskAssessmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risk-assessments/{id}")
    public ResponseEntity<RiskAssessmentDTO> getRiskAssessment(@PathVariable String id) {
        log.debug("REST request to get RiskAssessment : {}", id);
        Optional<RiskAssessmentDTO> riskAssessmentDTO = riskAssessmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(riskAssessmentDTO);
    }

    /**
     * {@code DELETE  /risk-assessments/:id} : delete the "id" riskAssessment.
     *
     * @param id the id of the riskAssessmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risk-assessments/{id}")
    public ResponseEntity<Void> deleteRiskAssessment(@PathVariable String id) {
        log.debug("REST request to delete RiskAssessment : {}", id);
        riskAssessmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
