package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.InspectionReport;
import be.fgov.famhp.imt.gateway.repository.InspectionReportRepository;
import be.fgov.famhp.imt.gateway.service.dto.InspectionReportDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionReportMapper;
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
 * Integration tests for the {@link InspectionReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InspectionReportResourceIT {

    private static final String ENTITY_API_URL = "/api/inspection-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionReportRepository inspectionReportRepository;

    @Autowired
    private InspectionReportMapper inspectionReportMapper;

    @Autowired
    private WebTestClient webTestClient;

    private InspectionReport inspectionReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionReport createEntity() {
        InspectionReport inspectionReport = new InspectionReport();
        return inspectionReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionReport createUpdatedEntity() {
        InspectionReport inspectionReport = new InspectionReport();
        return inspectionReport;
    }

    @BeforeEach
    public void initTest() {
        inspectionReportRepository.deleteAll().block();
        inspectionReport = createEntity();
    }

    @Test
    void createInspectionReport() throws Exception {
        int databaseSizeBeforeCreate = inspectionReportRepository.findAll().collectList().block().size();
        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeCreate + 1);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void createInspectionReportWithExistingId() throws Exception {
        // Create the InspectionReport with an existing ID
        inspectionReport.setId("existing_id");
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        int databaseSizeBeforeCreate = inspectionReportRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectionReportsAsStream() {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        List<InspectionReport> inspectionReportList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InspectionReportDTO.class)
            .getResponseBody()
            .map(inspectionReportMapper::toEntity)
            .filter(inspectionReport::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inspectionReportList).isNotNull();
        assertThat(inspectionReportList).hasSize(1);
        InspectionReport testInspectionReport = inspectionReportList.get(0);
    }

    @Test
    void getAllInspectionReports() {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        // Get all the inspectionReportList
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
            .value(hasItem(inspectionReport.getId()));
    }

    @Test
    void getInspectionReport() {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        // Get the inspectionReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inspectionReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inspectionReport.getId()));
    }

    @Test
    void getNonExistingInspectionReport() {
        // Get the inspectionReport
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInspectionReport() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();

        // Update the inspectionReport
        InspectionReport updatedInspectionReport = inspectionReportRepository.findById(inspectionReport.getId()).block();
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(updatedInspectionReport);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionReportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void putNonExistingInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionReportDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectionReportWithPatch() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();

        // Update the inspectionReport using partial update
        InspectionReport partialUpdatedInspectionReport = new InspectionReport();
        partialUpdatedInspectionReport.setId(inspectionReport.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectionReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void fullUpdateInspectionReportWithPatch() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();

        // Update the inspectionReport using partial update
        InspectionReport partialUpdatedInspectionReport = new InspectionReport();
        partialUpdatedInspectionReport.setId(inspectionReport.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectionReport.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionReport))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void patchNonExistingInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inspectionReportDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().collectList().block().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // Create the InspectionReport
        InspectionReportDTO inspectionReportDTO = inspectionReportMapper.toDto(inspectionReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionReportDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectionReport() {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport).block();

        int databaseSizeBeforeDelete = inspectionReportRepository.findAll().collectList().block().size();

        // Delete the inspectionReport
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inspectionReport.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll().collectList().block();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
