package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.CapaDocumentService;
import be.fgov.famhp.imt.backoffice.service.dto.CapaDocumentDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.CapaDocument}.
 */
@RestController
@RequestMapping("/api")
public class CapaDocumentResource {

    private final Logger log = LoggerFactory.getLogger(CapaDocumentResource.class);

    private static final String ENTITY_NAME = "imtBackofficeCapaDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CapaDocumentService capaDocumentService;

    private final CapaDocumentRepository capaDocumentRepository;

    public CapaDocumentResource(CapaDocumentService capaDocumentService, CapaDocumentRepository capaDocumentRepository) {
        this.capaDocumentService = capaDocumentService;
        this.capaDocumentRepository = capaDocumentRepository;
    }

    /**
     * {@code POST  /capa-documents} : Create a new capaDocument.
     *
     * @param capaDocumentDTO the capaDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capaDocumentDTO, or with status {@code 400 (Bad Request)} if the capaDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capa-documents")
    public ResponseEntity<CapaDocumentDTO> createCapaDocument(@RequestBody CapaDocumentDTO capaDocumentDTO) throws URISyntaxException {
        log.debug("REST request to save CapaDocument : {}", capaDocumentDTO);
        if (capaDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new capaDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CapaDocumentDTO result = capaDocumentService.save(capaDocumentDTO);
        return ResponseEntity
            .created(new URI("/api/capa-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /capa-documents/:id} : Updates an existing capaDocument.
     *
     * @param id the id of the capaDocumentDTO to save.
     * @param capaDocumentDTO the capaDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the capaDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capaDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capa-documents/{id}")
    public ResponseEntity<CapaDocumentDTO> updateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocumentDTO capaDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CapaDocument : {}, {}", id, capaDocumentDTO);
        if (capaDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CapaDocumentDTO result = capaDocumentService.update(capaDocumentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capaDocumentDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /capa-documents/:id} : Partial updates given fields of an existing capaDocument, field will ignore if it is null
     *
     * @param id the id of the capaDocumentDTO to save.
     * @param capaDocumentDTO the capaDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the capaDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the capaDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the capaDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capa-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CapaDocumentDTO> partialUpdateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocumentDTO capaDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapaDocument partially : {}, {}", id, capaDocumentDTO);
        if (capaDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CapaDocumentDTO> result = capaDocumentService.partialUpdate(capaDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capaDocumentDTO.getId())
        );
    }

    /**
     * {@code GET  /capa-documents} : get all the capaDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capaDocuments in body.
     */
    @GetMapping("/capa-documents")
    public List<CapaDocumentDTO> getAllCapaDocuments() {
        log.debug("REST request to get all CapaDocuments");
        return capaDocumentService.findAll();
    }

    /**
     * {@code GET  /capa-documents/:id} : get the "id" capaDocument.
     *
     * @param id the id of the capaDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capaDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capa-documents/{id}")
    public ResponseEntity<CapaDocumentDTO> getCapaDocument(@PathVariable String id) {
        log.debug("REST request to get CapaDocument : {}", id);
        Optional<CapaDocumentDTO> capaDocumentDTO = capaDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capaDocumentDTO);
    }

    /**
     * {@code DELETE  /capa-documents/:id} : delete the "id" capaDocument.
     *
     * @param id the id of the capaDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capa-documents/{id}")
    public ResponseEntity<Void> deleteCapaDocument(@PathVariable String id) {
        log.debug("REST request to delete CapaDocument : {}", id);
        capaDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
