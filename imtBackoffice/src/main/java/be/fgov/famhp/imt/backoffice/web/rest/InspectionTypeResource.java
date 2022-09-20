package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionTypeService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionTypeDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.InspectionType}.
 */
@RestController
@RequestMapping("/api")
public class InspectionTypeResource {

    private final Logger log = LoggerFactory.getLogger(InspectionTypeResource.class);

    private static final String ENTITY_NAME = "imtBackofficeInspectionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InspectionTypeService inspectionTypeService;

    private final InspectionTypeRepository inspectionTypeRepository;

    public InspectionTypeResource(InspectionTypeService inspectionTypeService, InspectionTypeRepository inspectionTypeRepository) {
        this.inspectionTypeService = inspectionTypeService;
        this.inspectionTypeRepository = inspectionTypeRepository;
    }

    /**
     * {@code POST  /inspection-types} : Create a new inspectionType.
     *
     * @param inspectionTypeDTO the inspectionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectionTypeDTO, or with status {@code 400 (Bad Request)} if the inspectionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspection-types")
    public ResponseEntity<InspectionTypeDTO> createInspectionType(@RequestBody InspectionTypeDTO inspectionTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save InspectionType : {}", inspectionTypeDTO);
        if (inspectionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspectionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspectionTypeDTO result = inspectionTypeService.save(inspectionTypeDTO);
        return ResponseEntity
            .created(new URI("/api/inspection-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspection-types/:id} : Updates an existing inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to save.
     * @param inspectionTypeDTO the inspectionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspection-types/{id}")
    public ResponseEntity<InspectionTypeDTO> updateInspectionType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionTypeDTO inspectionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InspectionType : {}, {}", id, inspectionTypeDTO);
        if (inspectionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspectionTypeDTO result = inspectionTypeService.update(inspectionTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionTypeDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspection-types/:id} : Partial updates given fields of an existing inspectionType, field will ignore if it is null
     *
     * @param id the id of the inspectionTypeDTO to save.
     * @param inspectionTypeDTO the inspectionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the inspectionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspection-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectionTypeDTO> partialUpdateInspectionType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectionTypeDTO inspectionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InspectionType partially : {}, {}", id, inspectionTypeDTO);
        if (inspectionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectionTypeDTO> result = inspectionTypeService.partialUpdate(inspectionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectionTypeDTO.getId())
        );
    }

    /**
     * {@code GET  /inspection-types} : get all the inspectionTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectionTypes in body.
     */
    @GetMapping("/inspection-types")
    public List<InspectionTypeDTO> getAllInspectionTypes() {
        log.debug("REST request to get all InspectionTypes");
        return inspectionTypeService.findAll();
    }

    /**
     * {@code GET  /inspection-types/:id} : get the "id" inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspection-types/{id}")
    public ResponseEntity<InspectionTypeDTO> getInspectionType(@PathVariable String id) {
        log.debug("REST request to get InspectionType : {}", id);
        Optional<InspectionTypeDTO> inspectionTypeDTO = inspectionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectionTypeDTO);
    }

    /**
     * {@code DELETE  /inspection-types/:id} : delete the "id" inspectionType.
     *
     * @param id the id of the inspectionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspection-types/{id}")
    public ResponseEntity<Void> deleteInspectionType(@PathVariable String id) {
        log.debug("REST request to delete InspectionType : {}", id);
        inspectionTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
