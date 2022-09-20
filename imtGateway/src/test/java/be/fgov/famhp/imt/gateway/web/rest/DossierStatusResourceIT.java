package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.DossierStatus;
import be.fgov.famhp.imt.gateway.repository.DossierStatusRepository;
import be.fgov.famhp.imt.gateway.service.dto.DossierStatusDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierStatusMapper;
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
 * Integration tests for the {@link DossierStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DossierStatusResourceIT {

    private static final String ENTITY_API_URL = "/api/dossier-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierStatusRepository dossierStatusRepository;

    @Autowired
    private DossierStatusMapper dossierStatusMapper;

    @Autowired
    private WebTestClient webTestClient;

    private DossierStatus dossierStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierStatus createEntity() {
        DossierStatus dossierStatus = new DossierStatus();
        return dossierStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierStatus createUpdatedEntity() {
        DossierStatus dossierStatus = new DossierStatus();
        return dossierStatus;
    }

    @BeforeEach
    public void initTest() {
        dossierStatusRepository.deleteAll().block();
        dossierStatus = createEntity();
    }

    @Test
    void createDossierStatus() throws Exception {
        int databaseSizeBeforeCreate = dossierStatusRepository.findAll().collectList().block().size();
        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeCreate + 1);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
    }

    @Test
    void createDossierStatusWithExistingId() throws Exception {
        // Create the DossierStatus with an existing ID
        dossierStatus.setId("existing_id");
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        int databaseSizeBeforeCreate = dossierStatusRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossierStatusesAsStream() {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        List<DossierStatus> dossierStatusList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DossierStatusDTO.class)
            .getResponseBody()
            .map(dossierStatusMapper::toEntity)
            .filter(dossierStatus::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(dossierStatusList).isNotNull();
        assertThat(dossierStatusList).hasSize(1);
        DossierStatus testDossierStatus = dossierStatusList.get(0);
    }

    @Test
    void getAllDossierStatuses() {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        // Get all the dossierStatusList
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
            .value(hasItem(dossierStatus.getId()));
    }

    @Test
    void getDossierStatus() {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        // Get the dossierStatus
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dossierStatus.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dossierStatus.getId()));
    }

    @Test
    void getNonExistingDossierStatus() {
        // Get the dossierStatus
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDossierStatus() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();

        // Update the dossierStatus
        DossierStatus updatedDossierStatus = dossierStatusRepository.findById(dossierStatus.getId()).block();
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(updatedDossierStatus);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierStatusDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
    }

    @Test
    void putNonExistingDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierStatusDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierStatusWithPatch() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();

        // Update the dossierStatus using partial update
        DossierStatus partialUpdatedDossierStatus = new DossierStatus();
        partialUpdatedDossierStatus.setId(dossierStatus.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossierStatus.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierStatus))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
    }

    @Test
    void fullUpdateDossierStatusWithPatch() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();

        // Update the dossierStatus using partial update
        DossierStatus partialUpdatedDossierStatus = new DossierStatus();
        partialUpdatedDossierStatus.setId(dossierStatus.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossierStatus.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierStatus))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
    }

    @Test
    void patchNonExistingDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dossierStatusDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().collectList().block().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // Create the DossierStatus
        DossierStatusDTO dossierStatusDTO = dossierStatusMapper.toDto(dossierStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierStatusDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossierStatus() {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus).block();

        int databaseSizeBeforeDelete = dossierStatusRepository.findAll().collectList().block().size();

        // Delete the dossierStatus
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dossierStatus.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll().collectList().block();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
