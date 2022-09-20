package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.CapaDocument;
import be.fgov.famhp.imt.gateway.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.gateway.service.dto.CapaDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.CapaDocumentMapper;
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
 * Integration tests for the {@link CapaDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CapaDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/capa-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CapaDocumentRepository capaDocumentRepository;

    @Autowired
    private CapaDocumentMapper capaDocumentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private CapaDocument capaDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapaDocument createEntity() {
        CapaDocument capaDocument = new CapaDocument();
        return capaDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapaDocument createUpdatedEntity() {
        CapaDocument capaDocument = new CapaDocument();
        return capaDocument;
    }

    @BeforeEach
    public void initTest() {
        capaDocumentRepository.deleteAll().block();
        capaDocument = createEntity();
    }

    @Test
    void createCapaDocument() throws Exception {
        int databaseSizeBeforeCreate = capaDocumentRepository.findAll().collectList().block().size();
        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void createCapaDocumentWithExistingId() throws Exception {
        // Create the CapaDocument with an existing ID
        capaDocument.setId("existing_id");
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        int databaseSizeBeforeCreate = capaDocumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCapaDocumentsAsStream() {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        List<CapaDocument> capaDocumentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CapaDocumentDTO.class)
            .getResponseBody()
            .map(capaDocumentMapper::toEntity)
            .filter(capaDocument::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(capaDocumentList).isNotNull();
        assertThat(capaDocumentList).hasSize(1);
        CapaDocument testCapaDocument = capaDocumentList.get(0);
    }

    @Test
    void getAllCapaDocuments() {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        // Get all the capaDocumentList
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
            .value(hasItem(capaDocument.getId()));
    }

    @Test
    void getCapaDocument() {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        // Get the capaDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, capaDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(capaDocument.getId()));
    }

    @Test
    void getNonExistingCapaDocument() {
        // Get the capaDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCapaDocument() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();

        // Update the capaDocument
        CapaDocument updatedCapaDocument = capaDocumentRepository.findById(capaDocument.getId()).block();
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(updatedCapaDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, capaDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void putNonExistingCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, capaDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCapaDocumentWithPatch() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();

        // Update the capaDocument using partial update
        CapaDocument partialUpdatedCapaDocument = new CapaDocument();
        partialUpdatedCapaDocument.setId(capaDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapaDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapaDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void fullUpdateCapaDocumentWithPatch() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();

        // Update the capaDocument using partial update
        CapaDocument partialUpdatedCapaDocument = new CapaDocument();
        partialUpdatedCapaDocument.setId(capaDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapaDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapaDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, capaDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().collectList().block().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // Create the CapaDocument
        CapaDocumentDTO capaDocumentDTO = capaDocumentMapper.toDto(capaDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCapaDocument() {
        // Initialize the database
        capaDocumentRepository.save(capaDocument).block();

        int databaseSizeBeforeDelete = capaDocumentRepository.findAll().collectList().block().size();

        // Delete the capaDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, capaDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll().collectList().block();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
