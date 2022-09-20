package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Inspector;
import be.fgov.famhp.imt.gateway.repository.InspectorRepository;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectorMapper;
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
 * Integration tests for the {@link InspectorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InspectorResourceIT {

    private static final String ENTITY_API_URL = "/api/inspectors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectorRepository inspectorRepository;

    @Autowired
    private InspectorMapper inspectorMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Inspector inspector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspector createEntity() {
        Inspector inspector = new Inspector();
        return inspector;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspector createUpdatedEntity() {
        Inspector inspector = new Inspector();
        return inspector;
    }

    @BeforeEach
    public void initTest() {
        inspectorRepository.deleteAll().block();
        inspector = createEntity();
    }

    @Test
    void createInspector() throws Exception {
        int databaseSizeBeforeCreate = inspectorRepository.findAll().collectList().block().size();
        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeCreate + 1);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void createInspectorWithExistingId() throws Exception {
        // Create the Inspector with an existing ID
        inspector.setId("existing_id");
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        int databaseSizeBeforeCreate = inspectorRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectorsAsStream() {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        List<Inspector> inspectorList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InspectorDTO.class)
            .getResponseBody()
            .map(inspectorMapper::toEntity)
            .filter(inspector::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inspectorList).isNotNull();
        assertThat(inspectorList).hasSize(1);
        Inspector testInspector = inspectorList.get(0);
    }

    @Test
    void getAllInspectors() {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        // Get all the inspectorList
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
            .value(hasItem(inspector.getId()));
    }

    @Test
    void getInspector() {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        // Get the inspector
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inspector.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inspector.getId()));
    }

    @Test
    void getNonExistingInspector() {
        // Get the inspector
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInspector() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();

        // Update the inspector
        Inspector updatedInspector = inspectorRepository.findById(inspector.getId()).block();
        InspectorDTO inspectorDTO = inspectorMapper.toDto(updatedInspector);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void putNonExistingInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectorDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectorWithPatch() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();

        // Update the inspector using partial update
        Inspector partialUpdatedInspector = new Inspector();
        partialUpdatedInspector.setId(inspector.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspector.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspector))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void fullUpdateInspectorWithPatch() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();

        // Update the inspector using partial update
        Inspector partialUpdatedInspector = new Inspector();
        partialUpdatedInspector.setId(inspector.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspector.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspector))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void patchNonExistingInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inspectorDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().collectList().block().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspector() {
        // Initialize the database
        inspectorRepository.save(inspector).block();

        int databaseSizeBeforeDelete = inspectorRepository.findAll().collectList().block().size();

        // Delete the inspector
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inspector.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Inspector> inspectorList = inspectorRepository.findAll().collectList().block();
        assertThat(inspectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
