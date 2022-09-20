package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import be.fgov.famhp.imt.backoffice.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.CapaDocumentService;
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
     * @param capaDocument the capaDocument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new capaDocument, or with status {@code 400 (Bad Request)} if the capaDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/capa-documents")
    public ResponseEntity<CapaDocument> createCapaDocument(@RequestBody CapaDocument capaDocument) throws URISyntaxException {
        log.debug("REST request to save CapaDocument : {}", capaDocument);
        if (capaDocument.getId() != null) {
            throw new BadRequestAlertException("A new capaDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CapaDocument result = capaDocumentService.save(capaDocument);
        return ResponseEntity
            .created(new URI("/api/capa-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /capa-documents/:id} : Updates an existing capaDocument.
     *
     * @param id the id of the capaDocument to save.
     * @param capaDocument the capaDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocument,
     * or with status {@code 400 (Bad Request)} if the capaDocument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the capaDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/capa-documents/{id}")
    public ResponseEntity<CapaDocument> updateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocument capaDocument
    ) throws URISyntaxException {
        log.debug("REST request to update CapaDocument : {}, {}", id, capaDocument);
        if (capaDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CapaDocument result = capaDocumentService.update(capaDocument);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capaDocument.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /capa-documents/:id} : Partial updates given fields of an existing capaDocument, field will ignore if it is null
     *
     * @param id the id of the capaDocument to save.
     * @param capaDocument the capaDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated capaDocument,
     * or with status {@code 400 (Bad Request)} if the capaDocument is not valid,
     * or with status {@code 404 (Not Found)} if the capaDocument is not found,
     * or with status {@code 500 (Internal Server Error)} if the capaDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/capa-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CapaDocument> partialUpdateCapaDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody CapaDocument capaDocument
    ) throws URISyntaxException {
        log.debug("REST request to partial update CapaDocument partially : {}, {}", id, capaDocument);
        if (capaDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, capaDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!capaDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CapaDocument> result = capaDocumentService.partialUpdate(capaDocument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, capaDocument.getId())
        );
    }

    /**
     * {@code GET  /capa-documents} : get all the capaDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of capaDocuments in body.
     */
    @GetMapping("/capa-documents")
    public List<CapaDocument> getAllCapaDocuments() {
        log.debug("REST request to get all CapaDocuments");
        return capaDocumentService.findAll();
    }

    /**
     * {@code GET  /capa-documents/:id} : get the "id" capaDocument.
     *
     * @param id the id of the capaDocument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the capaDocument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/capa-documents/{id}")
    public ResponseEntity<CapaDocument> getCapaDocument(@PathVariable String id) {
        log.debug("REST request to get CapaDocument : {}", id);
        Optional<CapaDocument> capaDocument = capaDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(capaDocument);
    }

    /**
     * {@code DELETE  /capa-documents/:id} : delete the "id" capaDocument.
     *
     * @param id the id of the capaDocument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/capa-documents/{id}")
    public ResponseEntity<Void> deleteCapaDocument(@PathVariable String id) {
        log.debug("REST request to delete CapaDocument : {}", id);
        capaDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
