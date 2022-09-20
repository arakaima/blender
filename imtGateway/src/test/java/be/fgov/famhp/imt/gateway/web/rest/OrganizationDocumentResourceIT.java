package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.OrganizationDocument;
import be.fgov.famhp.imt.gateway.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.gateway.service.dto.OrganizationDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.OrganizationDocumentMapper;
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
 * Integration tests for the {@link OrganizationDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrganizationDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/organization-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrganizationDocumentRepository organizationDocumentRepository;

    @Autowired
    private OrganizationDocumentMapper organizationDocumentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private OrganizationDocument organizationDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationDocument createEntity() {
        OrganizationDocument organizationDocument = new OrganizationDocument();
        return organizationDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationDocument createUpdatedEntity() {
        OrganizationDocument organizationDocument = new OrganizationDocument();
        return organizationDocument;
    }

    @BeforeEach
    public void initTest() {
        organizationDocumentRepository.deleteAll().block();
        organizationDocument = createEntity();
    }

    @Test
    void createOrganizationDocument() throws Exception {
        int databaseSizeBeforeCreate = organizationDocumentRepository.findAll().collectList().block().size();
        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void createOrganizationDocumentWithExistingId() throws Exception {
        // Create the OrganizationDocument with an existing ID
        organizationDocument.setId("existing_id");
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        int databaseSizeBeforeCreate = organizationDocumentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrganizationDocumentsAsStream() {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        List<OrganizationDocument> organizationDocumentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(OrganizationDocumentDTO.class)
            .getResponseBody()
            .map(organizationDocumentMapper::toEntity)
            .filter(organizationDocument::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(organizationDocumentList).isNotNull();
        assertThat(organizationDocumentList).hasSize(1);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(0);
    }

    @Test
    void getAllOrganizationDocuments() {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        // Get all the organizationDocumentList
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
            .value(hasItem(organizationDocument.getId()));
    }

    @Test
    void getOrganizationDocument() {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        // Get the organizationDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, organizationDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(organizationDocument.getId()));
    }

    @Test
    void getNonExistingOrganizationDocument() {
        // Get the organizationDocument
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOrganizationDocument() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();

        // Update the organizationDocument
        OrganizationDocument updatedOrganizationDocument = organizationDocumentRepository.findById(organizationDocument.getId()).block();
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(updatedOrganizationDocument);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void putNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrganizationDocumentWithPatch() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();

        // Update the organizationDocument using partial update
        OrganizationDocument partialUpdatedOrganizationDocument = new OrganizationDocument();
        partialUpdatedOrganizationDocument.setId(organizationDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganizationDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void fullUpdateOrganizationDocumentWithPatch() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();

        // Update the organizationDocument using partial update
        OrganizationDocument partialUpdatedOrganizationDocument = new OrganizationDocument();
        partialUpdatedOrganizationDocument.setId(organizationDocument.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganizationDocument.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationDocument))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().collectList().block().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrganizationDocument() {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument).block();

        int databaseSizeBeforeDelete = organizationDocumentRepository.findAll().collectList().block().size();

        // Delete the organizationDocument
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, organizationDocument.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll().collectList().block();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
