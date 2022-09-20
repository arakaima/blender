package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import be.fgov.famhp.imt.backoffice.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.AttachedDocumentService;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.AttachedDocument}.
 */
@RestController
@RequestMapping("/api")
public class AttachedDocumentResource {

    private final Logger log = LoggerFactory.getLogger(AttachedDocumentResource.class);

    private static final String ENTITY_NAME = "imtBackofficeAttachedDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachedDocumentService attachedDocumentService;

    private final AttachedDocumentRepository attachedDocumentRepository;

    public AttachedDocumentResource(
        AttachedDocumentService attachedDocumentService,
        AttachedDocumentRepository attachedDocumentRepository
    ) {
        this.attachedDocumentService = attachedDocumentService;
        this.attachedDocumentRepository = attachedDocumentRepository;
    }

    /**
     * {@code POST  /attached-documents} : Create a new attachedDocument.
     *
     * @param attachedDocument the attachedDocument to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachedDocument, or with status {@code 400 (Bad Request)} if the attachedDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attached-documents")
    public ResponseEntity<AttachedDocument> createAttachedDocument(@RequestBody AttachedDocument attachedDocument)
        throws URISyntaxException {
        log.debug("REST request to save AttachedDocument : {}", attachedDocument);
        if (attachedDocument.getId() != null) {
            throw new BadRequestAlertException("A new attachedDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttachedDocument result = attachedDocumentService.save(attachedDocument);
        return ResponseEntity
            .created(new URI("/api/attached-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /attached-documents/:id} : Updates an existing attachedDocument.
     *
     * @param id the id of the attachedDocument to save.
     * @param attachedDocument the attachedDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedDocument,
     * or with status {@code 400 (Bad Request)} if the attachedDocument is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachedDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attached-documents/{id}")
    public ResponseEntity<AttachedDocument> updateAttachedDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttachedDocument attachedDocument
    ) throws URISyntaxException {
        log.debug("REST request to update AttachedDocument : {}, {}", id, attachedDocument);
        if (attachedDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachedDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AttachedDocument result = attachedDocumentService.update(attachedDocument);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachedDocument.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /attached-documents/:id} : Partial updates given fields of an existing attachedDocument, field will ignore if it is null
     *
     * @param id the id of the attachedDocument to save.
     * @param attachedDocument the attachedDocument to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachedDocument,
     * or with status {@code 400 (Bad Request)} if the attachedDocument is not valid,
     * or with status {@code 404 (Not Found)} if the attachedDocument is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachedDocument couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attached-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AttachedDocument> partialUpdateAttachedDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody AttachedDocument attachedDocument
    ) throws URISyntaxException {
        log.debug("REST request to partial update AttachedDocument partially : {}, {}", id, attachedDocument);
        if (attachedDocument.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachedDocument.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!attachedDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AttachedDocument> result = attachedDocumentService.partialUpdate(attachedDocument);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attachedDocument.getId())
        );
    }

    /**
     * {@code GET  /attached-documents} : get all the attachedDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachedDocuments in body.
     */
    @GetMapping("/attached-documents")
    public List<AttachedDocument> getAllAttachedDocuments() {
        log.debug("REST request to get all AttachedDocuments");
        return attachedDocumentService.findAll();
    }

    /**
     * {@code GET  /attached-documents/:id} : get the "id" attachedDocument.
     *
     * @param id the id of the attachedDocument to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachedDocument, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attached-documents/{id}")
    public ResponseEntity<AttachedDocument> getAttachedDocument(@PathVariable String id) {
        log.debug("REST request to get AttachedDocument : {}", id);
        Optional<AttachedDocument> attachedDocument = attachedDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attachedDocument);
    }

    /**
     * {@code DELETE  /attached-documents/:id} : delete the "id" attachedDocument.
     *
     * @param id the id of the attachedDocument to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attached-documents/{id}")
    public ResponseEntity<Void> deleteAttachedDocument(@PathVariable String id) {
        log.debug("REST request to delete AttachedDocument : {}", id);
        attachedDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
