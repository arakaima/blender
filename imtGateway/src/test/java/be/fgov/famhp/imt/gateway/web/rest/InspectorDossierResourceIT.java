package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.InspectorDossier;
import be.fgov.famhp.imt.gateway.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDossierDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectorDossierMapper;
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
 * Integration tests for the {@link InspectorDossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InspectorDossierResourceIT {

    private static final String ENTITY_API_URL = "/api/inspector-dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectorDossierRepository inspectorDossierRepository;

    @Autowired
    private InspectorDossierMapper inspectorDossierMapper;

    @Autowired
    private WebTestClient webTestClient;

    private InspectorDossier inspectorDossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectorDossier createEntity() {
        InspectorDossier inspectorDossier = new InspectorDossier();
        return inspectorDossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectorDossier createUpdatedEntity() {
        InspectorDossier inspectorDossier = new InspectorDossier();
        return inspectorDossier;
    }

    @BeforeEach
    public void initTest() {
        inspectorDossierRepository.deleteAll().block();
        inspectorDossier = createEntity();
    }

    @Test
    void createInspectorDossier() throws Exception {
        int databaseSizeBeforeCreate = inspectorDossierRepository.findAll().collectList().block().size();
        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeCreate + 1);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void createInspectorDossierWithExistingId() throws Exception {
        // Create the InspectorDossier with an existing ID
        inspectorDossier.setId("existing_id");
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        int databaseSizeBeforeCreate = inspectorDossierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectorDossiersAsStream() {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        List<InspectorDossier> inspectorDossierList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InspectorDossierDTO.class)
            .getResponseBody()
            .map(inspectorDossierMapper::toEntity)
            .filter(inspectorDossier::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inspectorDossierList).isNotNull();
        assertThat(inspectorDossierList).hasSize(1);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(0);
    }

    @Test
    void getAllInspectorDossiers() {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        // Get all the inspectorDossierList
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
            .value(hasItem(inspectorDossier.getId()));
    }

    @Test
    void getInspectorDossier() {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        // Get the inspectorDossier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inspectorDossier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inspectorDossier.getId()));
    }

    @Test
    void getNonExistingInspectorDossier() {
        // Get the inspectorDossier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInspectorDossier() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();

        // Update the inspectorDossier
        InspectorDossier updatedInspectorDossier = inspectorDossierRepository.findById(inspectorDossier.getId()).block();
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(updatedInspectorDossier);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void putNonExistingInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectorDossierWithPatch() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();

        // Update the inspectorDossier using partial update
        InspectorDossier partialUpdatedInspectorDossier = new InspectorDossier();
        partialUpdatedInspectorDossier.setId(inspectorDossier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectorDossier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectorDossier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void fullUpdateInspectorDossierWithPatch() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();

        // Update the inspectorDossier using partial update
        InspectorDossier partialUpdatedInspectorDossier = new InspectorDossier();
        partialUpdatedInspectorDossier.setId(inspectorDossier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectorDossier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectorDossier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void patchNonExistingInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().collectList().block().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectorDossier() {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier).block();

        int databaseSizeBeforeDelete = inspectorDossierRepository.findAll().collectList().block().size();

        // Delete the inspectorDossier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inspectorDossier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll().collectList().block();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
