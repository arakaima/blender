package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import be.fgov.famhp.imt.backoffice.repository.CapaRepository;
import be.fgov.famhp.imt.backoffice.service.CapaService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Capa}.
 */
@RestController
@RequestMapping("/api")
public class CapaResource {

    private final Logger log = LoggerFactory.getLogger(CapaResource.class);

    private static final String ENTITY_NAME = "imtBackofficeCapa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapaService capaService;

    private final CapaRepository capaRepository;

    public CapaResource(CapaService capaService, CapaRepository capaRepository) {
        this.capaService = capaService;
        this.capaRepository = capaRepository;
    }

    /**
     * {@code POST  /capas} : Create a new capa.
     *
     * @param capa the capa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capa, or with status {@code 400 (Bad Request)} if the capa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capas")
    public ResponseEntity<Capa> createCapa(@RequestBody Capa capa) throws URISyntaxException {
        log.debug("REST request to save Capa : {}", capa);
        if (capa.getId() != null) {
            throw new BadRequestAlertException("A new capa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Capa result = capaService.save(capa);
        return ResponseEntity
            .created(new URI("/api/capas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /capas/:id} : Updates an existing capa.
     *
     * @param id the id of the capa to save.
     * @param capa the capa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capa,
     * or with status {@code 400 (Bad Request)} if the capa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capas/{id}")
    public ResponseEntity<Capa> updateCapa(@PathVariable(value = "id", required = false) final String id, @RequestBody Capa capa)
        throws URISyntaxException {
        log.debug("REST request to update Capa : {}, {}", id, capa);
        if (capa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Capa result = capaService.update(capa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capa.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /capas/:id} : Partial updates given fields of an existing capa, field will ignore if it is null
     *
     * @param id the id of the capa to save.
     * @param capa the capa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capa,
     * or with status {@code 400 (Bad Request)} if the capa is not valid,
     * or with status {@code 404 (Not Found)} if the capa is not found,
     * or with status {@code 500 (Internal Server Error)} if the capa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Capa> partialUpdateCapa(@PathVariable(value = "id", required = false) final String id, @RequestBody Capa capa)
        throws URISyntaxException {
        log.debug("REST request to partial update Capa partially : {}, {}", id, capa);
        if (capa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Capa> result = capaService.partialUpdate(capa);

        return ResponseUtil.wrapOrNotFound(result, HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capa.getId()));
    }

    /**
     * {@code GET  /capas} : get all the capas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capas in body.
     */
    @GetMapping("/capas")
    public List<Capa> getAllCapas() {
        log.debug("REST request to get all Capas");
        return capaService.findAll();
    }

    /**
     * {@code GET  /capas/:id} : get the "id" capa.
     *
     * @param id the id of the capa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capas/{id}")
    public ResponseEntity<Capa> getCapa(@PathVariable String id) {
        log.debug("REST request to get Capa : {}", id);
        Optional<Capa> capa = capaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capa);
    }

    /**
     * {@code DELETE  /capas/:id} : delete the "id" capa.
     *
     * @param id the id of the capa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capas/{id}")
    public ResponseEntity<Void> deleteCapa(@PathVariable String id) {
        log.debug("REST request to delete Capa : {}", id);
        capaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
