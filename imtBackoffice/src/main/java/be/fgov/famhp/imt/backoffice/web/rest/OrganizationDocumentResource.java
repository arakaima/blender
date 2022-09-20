package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.OrganizationDocumentService;
import be.fgov.famhp.imt.backoffice.service.dto.OrganizationDocumentDTO;
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
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.OrganizationDocument}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationDocumentResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationDocumentResource.class);

    private static final String ENTITY_NAME = "imtBackofficeOrganizationDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationDocumentService organizationDocumentService;

    private final OrganizationDocumentRepository organizationDocumentRepository;

    public OrganizationDocumentResource(
        OrganizationDocumentService organizationDocumentService,
        OrganizationDocumentRepository organizationDocumentRepository
    ) {
        this.organizationDocumentService = organizationDocumentService;
        this.organizationDocumentRepository = organizationDocumentRepository;
    }

    /**
     * {@code POST  /organization-documents} : Create a new organizationDocument.
     *
     * @param organizationDocumentDTO the organizationDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationDocumentDTO, or with status {@code 400 (Bad Request)} if the organizationDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organization-documents")
    public ResponseEntity<OrganizationDocumentDTO> createOrganizationDocument(@RequestBody OrganizationDocumentDTO organizationDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save OrganizationDocument : {}", organizationDocumentDTO);
        if (organizationDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new organizationDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationDocumentDTO result = organizationDocumentService.save(organizationDocumentDTO);
        return ResponseEntity
            .created(new URI("/api/organization-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /organization-documents/:id} : Updates an existing organizationDocument.
     *
     * @param id the id of the organizationDocumentDTO to save.
     * @param organizationDocumentDTO the organizationDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organization-documents/{id}")
    public ResponseEntity<OrganizationDocumentDTO> updateOrganizationDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody OrganizationDocumentDTO organizationDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OrganizationDocument : {}, {}", id, organizationDocumentDTO);
        if (organizationDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrganizationDocumentDTO result = organizationDocumentService.update(organizationDocumentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDocumentDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /organization-documents/:id} : Partial updates given fields of an existing organizationDocument, field will ignore if it is null
     *
     * @param id the id of the organizationDocumentDTO to save.
     * @param organizationDocumentDTO the organizationDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the organizationDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the organizationDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/organization-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrganizationDocumentDTO> partialUpdateOrganizationDocument(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody OrganizationDocumentDTO organizationDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrganizationDocument partially : {}, {}", id, organizationDocumentDTO);
        if (organizationDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, organizationDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!organizationDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrganizationDocumentDTO> result = organizationDocumentService.partialUpdate(organizationDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDocumentDTO.getId())
        );
    }

    /**
     * {@code GET  /organization-documents} : get all the organizationDocuments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizationDocuments in body.
     */
    @GetMapping("/organization-documents")
    public List<OrganizationDocumentDTO> getAllOrganizationDocuments() {
        log.debug("REST request to get all OrganizationDocuments");
        return organizationDocumentService.findAll();
    }

    /**
     * {@code GET  /organization-documents/:id} : get the "id" organizationDocument.
     *
     * @param id the id of the organizationDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organization-documents/{id}")
    public ResponseEntity<OrganizationDocumentDTO> getOrganizationDocument(@PathVariable String id) {
        log.debug("REST request to get OrganizationDocument : {}", id);
        Optional<OrganizationDocumentDTO> organizationDocumentDTO = organizationDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationDocumentDTO);
    }

    /**
     * {@code DELETE  /organization-documents/:id} : delete the "id" organizationDocument.
     *
     * @param id the id of the organizationDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organization-documents/{id}")
    public ResponseEntity<Void> deleteOrganizationDocument(@PathVariable String id) {
        log.debug("REST request to delete OrganizationDocument : {}", id);
        organizationDocumentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
