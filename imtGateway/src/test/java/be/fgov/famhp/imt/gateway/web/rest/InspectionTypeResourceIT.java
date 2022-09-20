package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.InspectionType;
import be.fgov.famhp.imt.gateway.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.gateway.service.dto.InspectionTypeDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionTypeMapper;
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
 * Integration tests for the {@link InspectionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class InspectionTypeResourceIT {

    private static final String ENTITY_API_URL = "/api/inspection-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionTypeRepository inspectionTypeRepository;

    @Autowired
    private InspectionTypeMapper inspectionTypeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private InspectionType inspectionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionType createEntity() {
        InspectionType inspectionType = new InspectionType();
        return inspectionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionType createUpdatedEntity() {
        InspectionType inspectionType = new InspectionType();
        return inspectionType;
    }

    @BeforeEach
    public void initTest() {
        inspectionTypeRepository.deleteAll().block();
        inspectionType = createEntity();
    }

    @Test
    void createInspectionType() throws Exception {
        int databaseSizeBeforeCreate = inspectionTypeRepository.findAll().collectList().block().size();
        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void createInspectionTypeWithExistingId() throws Exception {
        // Create the InspectionType with an existing ID
        inspectionType.setId("existing_id");
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        int databaseSizeBeforeCreate = inspectionTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectionTypesAsStream() {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        List<InspectionType> inspectionTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(InspectionTypeDTO.class)
            .getResponseBody()
            .map(inspectionTypeMapper::toEntity)
            .filter(inspectionType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inspectionTypeList).isNotNull();
        assertThat(inspectionTypeList).hasSize(1);
        InspectionType testInspectionType = inspectionTypeList.get(0);
    }

    @Test
    void getAllInspectionTypes() {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        // Get all the inspectionTypeList
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
            .value(hasItem(inspectionType.getId()));
    }

    @Test
    void getInspectionType() {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        // Get the inspectionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inspectionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inspectionType.getId()));
    }

    @Test
    void getNonExistingInspectionType() {
        // Get the inspectionType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingInspectionType() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();

        // Update the inspectionType
        InspectionType updatedInspectionType = inspectionTypeRepository.findById(inspectionType.getId()).block();
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(updatedInspectionType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void putNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectionTypeWithPatch() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();

        // Update the inspectionType using partial update
        InspectionType partialUpdatedInspectionType = new InspectionType();
        partialUpdatedInspectionType.setId(inspectionType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void fullUpdateInspectionTypeWithPatch() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();

        // Update the inspectionType using partial update
        InspectionType partialUpdatedInspectionType = new InspectionType();
        partialUpdatedInspectionType.setId(inspectionType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInspectionType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void patchNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().collectList().block().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectionType() {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType).block();

        int databaseSizeBeforeDelete = inspectionTypeRepository.findAll().collectList().block().size();

        // Delete the inspectionType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inspectionType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll().collectList().block();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
