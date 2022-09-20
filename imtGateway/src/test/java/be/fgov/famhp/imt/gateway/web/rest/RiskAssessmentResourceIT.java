package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.RiskAssessment;
import be.fgov.famhp.imt.gateway.repository.RiskAssessmentRepository;
import be.fgov.famhp.imt.gateway.service.dto.RiskAssessmentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.RiskAssessmentMapper;
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
 * Integration tests for the {@link RiskAssessmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RiskAssessmentResourceIT {

    private static final String ENTITY_API_URL = "/api/risk-assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RiskAssessmentRepository riskAssessmentRepository;

    @Autowired
    private RiskAssessmentMapper riskAssessmentMapper;

    @Autowired
    private WebTestClient webTestClient;

    private RiskAssessment riskAssessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createEntity() {
        RiskAssessment riskAssessment = new RiskAssessment();
        return riskAssessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createUpdatedEntity() {
        RiskAssessment riskAssessment = new RiskAssessment();
        return riskAssessment;
    }

    @BeforeEach
    public void initTest() {
        riskAssessmentRepository.deleteAll().block();
        riskAssessment = createEntity();
    }

    @Test
    void createRiskAssessment() throws Exception {
        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().collectList().block().size();
        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate + 1);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void createRiskAssessmentWithExistingId() throws Exception {
        // Create the RiskAssessment with an existing ID
        riskAssessment.setId("existing_id");
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRiskAssessmentsAsStream() {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        List<RiskAssessment> riskAssessmentList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RiskAssessmentDTO.class)
            .getResponseBody()
            .map(riskAssessmentMapper::toEntity)
            .filter(riskAssessment::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(riskAssessmentList).isNotNull();
        assertThat(riskAssessmentList).hasSize(1);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(0);
    }

    @Test
    void getAllRiskAssessments() {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        // Get all the riskAssessmentList
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
            .value(hasItem(riskAssessment.getId()));
    }

    @Test
    void getRiskAssessment() {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        // Get the riskAssessment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, riskAssessment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(riskAssessment.getId()));
    }

    @Test
    void getNonExistingRiskAssessment() {
        // Get the riskAssessment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();

        // Update the riskAssessment
        RiskAssessment updatedRiskAssessment = riskAssessmentRepository.findById(riskAssessment.getId()).block();
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(updatedRiskAssessment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, riskAssessmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void putNonExistingRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, riskAssessmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRiskAssessmentWithPatch() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();

        // Update the riskAssessment using partial update
        RiskAssessment partialUpdatedRiskAssessment = new RiskAssessment();
        partialUpdatedRiskAssessment.setId(riskAssessment.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRiskAssessment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRiskAssessment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void fullUpdateRiskAssessmentWithPatch() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();

        // Update the riskAssessment using partial update
        RiskAssessment partialUpdatedRiskAssessment = new RiskAssessment();
        partialUpdatedRiskAssessment.setId(riskAssessment.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRiskAssessment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRiskAssessment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void patchNonExistingRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, riskAssessmentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().collectList().block().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // Create the RiskAssessment
        RiskAssessmentDTO riskAssessmentDTO = riskAssessmentMapper.toDto(riskAssessment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(riskAssessmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRiskAssessment() {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment).block();

        int databaseSizeBeforeDelete = riskAssessmentRepository.findAll().collectList().block().size();

        // Delete the riskAssessment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, riskAssessment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll().collectList().block();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
