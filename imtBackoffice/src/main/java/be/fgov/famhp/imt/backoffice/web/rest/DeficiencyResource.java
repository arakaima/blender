package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.DeficiencyRepository;
import be.fgov.famhp.imt.backoffice.service.DeficiencyService;
import be.fgov.famhp.imt.backoffice.service.dto.DeficiencyDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Deficiency}.
 */
@RestController
@RequestMapping("/api")
public class DeficiencyResource {

    private final Logger log = LoggerFactory.getLogger(DeficiencyResource.class);

    private static final String ENTITY_NAME = "imtBackofficeDeficiency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeficiencyService deficiencyService;

    private final DeficiencyRepository deficiencyRepository;

    public DeficiencyResource(DeficiencyService deficiencyService, DeficiencyRepository deficiencyRepository) {
        this.deficiencyService = deficiencyService;
        this.deficiencyRepository = deficiencyRepository;
    }

    /**
     * {@code POST  /deficiencies} : Create a new deficiency.
     *
     * @param deficiencyDTO the deficiencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deficiencyDTO, or with status {@code 400 (Bad Request)} if the deficiency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/deficiencies")
    public ResponseEntity<DeficiencyDTO> createDeficiency(@RequestBody DeficiencyDTO deficiencyDTO) throws URISyntaxException {
        log.debug("REST request to save Deficiency : {}", deficiencyDTO);
        if (deficiencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new deficiency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeficiencyDTO result = deficiencyService.save(deficiencyDTO);
        return ResponseEntity
            .created(new URI("/api/deficiencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /deficiencies/:id} : Updates an existing deficiency.
     *
     * @param id the id of the deficiencyDTO to save.
     * @param deficiencyDTO the deficiencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deficiencyDTO,
     * or with status {@code 400 (Bad Request)} if the deficiencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deficiencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/deficiencies/{id}")
    public ResponseEntity<DeficiencyDTO> updateDeficiency(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeficiencyDTO deficiencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Deficiency : {}, {}", id, deficiencyDTO);
        if (deficiencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deficiencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deficiencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeficiencyDTO result = deficiencyService.update(deficiencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deficiencyDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /deficiencies/:id} : Partial updates given fields of an existing deficiency, field will ignore if it is null
     *
     * @param id the id of the deficiencyDTO to save.
     * @param deficiencyDTO the deficiencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deficiencyDTO,
     * or with status {@code 400 (Bad Request)} if the deficiencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deficiencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deficiencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/deficiencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeficiencyDTO> partialUpdateDeficiency(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody DeficiencyDTO deficiencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Deficiency partially : {}, {}", id, deficiencyDTO);
        if (deficiencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deficiencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deficiencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeficiencyDTO> result = deficiencyService.partialUpdate(deficiencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deficiencyDTO.getId())
        );
    }

    /**
     * {@code GET  /deficiencies} : get all the deficiencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deficiencies in body.
     */
    @GetMapping("/deficiencies")
    public List<DeficiencyDTO> getAllDeficiencies() {
        log.debug("REST request to get all Deficiencies");
        return deficiencyService.findAll();
    }

    /**
     * {@code GET  /deficiencies/:id} : get the "id" deficiency.
     *
     * @param id the id of the deficiencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deficiencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/deficiencies/{id}")
    public ResponseEntity<DeficiencyDTO> getDeficiency(@PathVariable String id) {
        log.debug("REST request to get Deficiency : {}", id);
        Optional<DeficiencyDTO> deficiencyDTO = deficiencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deficiencyDTO);
    }

    /**
     * {@code DELETE  /deficiencies/:id} : delete the "id" deficiency.
     *
     * @param id the id of the deficiencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/deficiencies/{id}")
    public ResponseEntity<Void> deleteDeficiency(@PathVariable String id) {
        log.debug("REST request to delete Deficiency : {}", id);
        deficiencyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
