package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.DossierTypeRepository;
import be.fgov.famhp.imt.backoffice.service.DossierTypeService;
import be.fgov.famhp.imt.backoffice.service.dto.DossierTypeDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.DossierType}.
 */
@RestController
@RequestMapping("/api")
public class DossierTypeResource {

    private final Logger log = LoggerFactory.getLogger(DossierTypeResource.class);

    private static final String ENTITY_NAME = "imtBackofficeDossierType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierTypeService dossierTypeService;

    private final DossierTypeRepository dossierTypeRepository;

    public DossierTypeResource(DossierTypeService dossierTypeService, DossierTypeRepository dossierTypeRepository) {
        this.dossierTypeService = dossierTypeService;
        this.dossierTypeRepository = dossierTypeRepository;
    }

    /**
     * {@code POST  /dossier-types} : Create a new dossierType.
     *
     * @param dossierTypeDTO the dossierTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierTypeDTO, or with status {@code 400 (Bad Request)} if the dossierType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossier-types")
    public ResponseEntity<DossierTypeDTO> createDossierType(@RequestBody DossierTypeDTO dossierTypeDTO) throws URISyntaxException {
        log.debug("REST request to save DossierType : {}", dossierTypeDTO);
        if (dossierTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossierType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierTypeDTO result = dossierTypeService.save(dossierTypeDTO);
        return ResponseEntity
            .created(new URI("/api/dossier-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /dossier-types/:id} : Updates an existing dossierType.
     *
     * @param id the id of the dossierTypeDTO to save.
     * @param dossierTypeDTO the dossierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the dossierTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossier-types/{id}")
    public ResponseEntity<DossierTypeDTO> updateDossierType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierTypeDTO dossierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DossierType : {}, {}", id, dossierTypeDTO);
        if (dossierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DossierTypeDTO result = dossierTypeService.update(dossierTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierTypeDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /dossier-types/:id} : Partial updates given fields of an existing dossierType, field will ignore if it is null
     *
     * @param id the id of the dossierTypeDTO to save.
     * @param dossierTypeDTO the dossierTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierTypeDTO,
     * or with status {@code 400 (Bad Request)} if the dossierTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dossierTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dossierTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dossier-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DossierTypeDTO> partialUpdateDossierType(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DossierTypeDTO dossierTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DossierType partially : {}, {}", id, dossierTypeDTO);
        if (dossierTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dossierTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dossierTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DossierTypeDTO> result = dossierTypeService.partialUpdate(dossierTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierTypeDTO.getId())
        );
    }

    /**
     * {@code GET  /dossier-types} : get all the dossierTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossierTypes in body.
     */
    @GetMapping("/dossier-types")
    public List<DossierTypeDTO> getAllDossierTypes() {
        log.debug("REST request to get all DossierTypes");
        return dossierTypeService.findAll();
    }

    /**
     * {@code GET  /dossier-types/:id} : get the "id" dossierType.
     *
     * @param id the id of the dossierTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossier-types/{id}")
    public ResponseEntity<DossierTypeDTO> getDossierType(@PathVariable String id) {
        log.debug("REST request to get DossierType : {}", id);
        Optional<DossierTypeDTO> dossierTypeDTO = dossierTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierTypeDTO);
    }

    /**
     * {@code DELETE  /dossier-types/:id} : delete the "id" dossierType.
     *
     * @param id the id of the dossierTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossier-types/{id}")
    public ResponseEntity<Void> deleteDossierType(@PathVariable String id) {
        log.debug("REST request to delete DossierType : {}", id);
        dossierTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
