package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.AttachedDocument;
import be.fgov.famhp.imt.gateway.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.gateway.service.dto.AttachedDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.AttachedDocumentMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link AttachedDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AttachedDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/attached-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttachedDocumentRepository attachedDocumentRepository;

    @Autowired
    private AttachedDocumentMapper attachedDocumentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private AttachedDocument attachedDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedDocument createEntity() {
        AttachedDocument attachedDocument = new AttachedDocument();
        return attachedDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedDocument createUpdatedEntity() {
        AttachedDocument attachedDocument = new AttachedDocument();
        return attachedDocument;
    }

    @BeforeEach
    public void initTest() {
        attachedDocumentRepository.deleteAll().block();
        attachedDocument = createEntity();
    }

    @Test
    void createAttachedDocument() throws Exception {
        int databaseSizeBeforeCreate = attachedDocumentRepository.findAll().collectList().block().size();
        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void createAttachedDocumentWithExistingId() throws Exception {
        // Create the AttachedDocument with an existing ID
        attachedDocument.setId("existing_id");
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        int databaseSizeBeforeCreate = attachedDocumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAttachedDocumentsAsStream() {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        List<AttachedDocument> attachedDocumentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(AttachedDocumentDTO.class)
            .getResponseBody()
            .map(attachedDocumentMapper::toEntity)
            .filter(attachedDocument::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(attachedDocumentList).isNotNull();
        assertThat(attachedDocumentList).hasSize(1);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(0);
    }

    @Test
    void getAllAttachedDocuments() {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        // Get all the attachedDocumentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(attachedDocument.getId()));
    }

    @Test
    void getAttachedDocument() {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        // Get the attachedDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attachedDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attachedDocument.getId()));
    }

    @Test
    void getNonExistingAttachedDocument() {
        // Get the attachedDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAttachedDocument() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();

        // Update the attachedDocument
        AttachedDocument updatedAttachedDocument = attachedDocumentRepository.findById(attachedDocument.getId()).block();
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(updatedAttachedDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attachedDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void putNonExistingAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attachedDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttachedDocumentWithPatch() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();

        // Update the attachedDocument using partial update
        AttachedDocument partialUpdatedAttachedDocument = new AttachedDocument();
        partialUpdatedAttachedDocument.setId(attachedDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttachedDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void fullUpdateAttachedDocumentWithPatch() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();

        // Update the attachedDocument using partial update
        AttachedDocument partialUpdatedAttachedDocument = new AttachedDocument();
        partialUpdatedAttachedDocument.setId(attachedDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttachedDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attachedDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().collectList().block().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // Create the AttachedDocument
        AttachedDocumentDTO attachedDocumentDTO = attachedDocumentMapper.toDto(attachedDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachedDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttachedDocument() {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument).block();

        int databaseSizeBeforeDelete = attachedDocumentRepository.findAll().collectList().block().size();

        // Delete the attachedDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attachedDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll().collectList().block();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
