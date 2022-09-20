package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.InspectorRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDTO;
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
     * @param inspectorDTO the inspectorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inspectorDTO, or with status {@code 400 (Bad Request)} if the inspector has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inspectors")
    public ResponseEntity<InspectorDTO> createInspector(@RequestBody InspectorDTO inspectorDTO) throws URISyntaxException {
        log.debug("REST request to save Inspector : {}", inspectorDTO);
        if (inspectorDTO.getId() != null) {
            throw new BadRequestAlertException("A new inspector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InspectorDTO result = inspectorService.save(inspectorDTO);
        return ResponseEntity
            .created(new URI("/api/inspectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /inspectors/:id} : Updates an existing inspector.
     *
     * @param id the id of the inspectorDTO to save.
     * @param inspectorDTO the inspectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inspectors/{id}")
    public ResponseEntity<InspectorDTO> updateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDTO inspectorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Inspector : {}, {}", id, inspectorDTO);
        if (inspectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InspectorDTO result = inspectorService.update(inspectorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectorDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /inspectors/:id} : Partial updates given fields of an existing inspector, field will ignore if it is null
     *
     * @param id the id of the inspectorDTO to save.
     * @param inspectorDTO the inspectorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inspectorDTO,
     * or with status {@code 400 (Bad Request)} if the inspectorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inspectorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inspectorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inspectors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InspectorDTO> partialUpdateInspector(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody InspectorDTO inspectorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inspector partially : {}, {}", id, inspectorDTO);
        if (inspectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inspectorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inspectorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InspectorDTO> result = inspectorService.partialUpdate(inspectorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, inspectorDTO.getId())
        );
    }

    /**
     * {@code GET  /inspectors} : get all the inspectors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inspectors in body.
     */
    @GetMapping("/inspectors")
    public List<InspectorDTO> getAllInspectors() {
        log.debug("REST request to get all Inspectors");
        return inspectorService.findAll();
    }

    /**
     * {@code GET  /inspectors/:id} : get the "id" inspector.
     *
     * @param id the id of the inspectorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inspectorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inspectors/{id}")
    public ResponseEntity<InspectorDTO> getInspector(@PathVariable String id) {
        log.debug("REST request to get Inspector : {}", id);
        Optional<InspectorDTO> inspectorDTO = inspectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inspectorDTO);
    }

    /**
     * {@code DELETE  /inspectors/:id} : delete the "id" inspector.
     *
     * @param id the id of the inspectorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inspectors/{id}")
    public ResponseEntity<Void> deleteInspector(@PathVariable String id) {
        log.debug("REST request to delete Inspector : {}", id);
        inspectorService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
