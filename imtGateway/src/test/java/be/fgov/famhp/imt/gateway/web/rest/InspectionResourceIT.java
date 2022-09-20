package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Inspection;
import be.fgov.famhp.imt.gateway.repository.InspectionRepository;
import be.fgov.famhp.imt.gateway.service.dto.InspectionDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionMapper;
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
 * Integration tests for the {@link InspectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InspectionResourceIT {

    private static final String ENTITY_API_URL = "/api/inspections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionRepository inspectionRepository;

    @Autowired
    private InspectionMapper inspectionMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Inspection inspection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createEntity() {
        Inspection inspection = new Inspection();
        return inspection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspection createUpdatedEntity() {
        Inspection inspection = new Inspection();
        return inspection;
    }

    @BeforeEach
    public void initTest() {
        inspectionRepository.deleteAll().block();
        inspection = createEntity();
    }

    @Test
    void createInspection() throws Exception {
        int databaseSizeBeforeCreate = inspectionRepository.findAll().collectList().block().size();
        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate + 1);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
    }

    @Test
    void createInspectionWithExistingId() throws Exception {
        // Create the Inspection with an existing ID
        inspection.setId("existing_id");
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        int databaseSizeBeforeCreate = inspectionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectionsAsStream() {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        List<Inspection> inspectionList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InspectionDTO.class)
            .getResponseBody()
            .map(inspectionMapper::toEntity)
            .filter(inspection::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inspectionList).isNotNull();
        assertThat(inspectionList).hasSize(1);
        Inspection testInspection = inspectionList.get(0);
    }

    @Test
    void getAllInspections() {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        // Get all the inspectionList
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
            .value(hasItem(inspection.getId()));
    }

    @Test
    void getInspection() {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        // Get the inspection
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inspection.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inspection.getId()));
    }

    @Test
    void getNonExistingInspection() {
        // Get the inspection
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInspection() throws Exception {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();

        // Update the inspection
        Inspection updatedInspection = inspectionRepository.findById(inspection.getId()).block();
        InspectionDTO inspectionDTO = inspectionMapper.toDto(updatedInspection);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
    }

    @Test
    void putNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
    }

    @Test
    void fullUpdateInspectionWithPatch() throws Exception {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();

        // Update the inspection using partial update
        Inspection partialUpdatedInspection = new Inspection();
        partialUpdatedInspection.setId(inspection.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspection.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspection))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
        Inspection testInspection = inspectionList.get(inspectionList.size() - 1);
    }

    @Test
    void patchNonExistingInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inspectionDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspection() throws Exception {
        int databaseSizeBeforeUpdate = inspectionRepository.findAll().collectList().block().size();
        inspection.setId(UUID.randomUUID().toString());

        // Create the Inspection
        InspectionDTO inspectionDTO = inspectionMapper.toDto(inspection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inspection in the database
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspection() {
        // Initialize the database
        inspectionRepository.save(inspection).block();

        int databaseSizeBeforeDelete = inspectionRepository.findAll().collectList().block().size();

        // Delete the inspection
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inspection.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Inspection> inspectionList = inspectionRepository.findAll().collectList().block();
        assertThat(inspectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
