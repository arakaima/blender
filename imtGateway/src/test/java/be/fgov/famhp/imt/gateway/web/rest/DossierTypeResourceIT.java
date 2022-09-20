package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.DossierType;
import be.fgov.famhp.imt.gateway.repository.DossierTypeRepository;
import be.fgov.famhp.imt.gateway.service.dto.DossierTypeDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierTypeMapper;
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
 * Integration tests for the {@link DossierTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DossierTypeResourceIT {

    private static final String ENTITY_API_URL = "/api/dossier-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierTypeRepository dossierTypeRepository;

    @Autowired
    private DossierTypeMapper dossierTypeMapper;

    @Autowired
    private WebTestClient webTestClient;

    private DossierType dossierType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierType createEntity() {
        DossierType dossierType = new DossierType();
        return dossierType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierType createUpdatedEntity() {
        DossierType dossierType = new DossierType();
        return dossierType;
    }

    @BeforeEach
    public void initTest() {
        dossierTypeRepository.deleteAll().block();
        dossierType = createEntity();
    }

    @Test
    void createDossierType() throws Exception {
        int databaseSizeBeforeCreate = dossierTypeRepository.findAll().collectList().block().size();
        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void createDossierTypeWithExistingId() throws Exception {
        // Create the DossierType with an existing ID
        dossierType.setId("existing_id");
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        int databaseSizeBeforeCreate = dossierTypeRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossierTypesAsStream() {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        List<DossierType> dossierTypeList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DossierTypeDTO.class)
            .getResponseBody()
            .map(dossierTypeMapper::toEntity)
            .filter(dossierType::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(dossierTypeList).isNotNull();
        assertThat(dossierTypeList).hasSize(1);
        DossierType testDossierType = dossierTypeList.get(0);
    }

    @Test
    void getAllDossierTypes() {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        // Get all the dossierTypeList
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
            .value(hasItem(dossierType.getId()));
    }

    @Test
    void getDossierType() {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        // Get the dossierType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dossierType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dossierType.getId()));
    }

    @Test
    void getNonExistingDossierType() {
        // Get the dossierType
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDossierType() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();

        // Update the dossierType
        DossierType updatedDossierType = dossierTypeRepository.findById(dossierType.getId()).block();
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(updatedDossierType);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void putNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierTypeDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierTypeWithPatch() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();

        // Update the dossierType using partial update
        DossierType partialUpdatedDossierType = new DossierType();
        partialUpdatedDossierType.setId(dossierType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossierType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void fullUpdateDossierTypeWithPatch() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();

        // Update the dossierType using partial update
        DossierType partialUpdatedDossierType = new DossierType();
        partialUpdatedDossierType.setId(dossierType.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossierType.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierType))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void patchNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dossierTypeDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().collectList().block().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossierType() {
        // Initialize the database
        dossierTypeRepository.save(dossierType).block();

        int databaseSizeBeforeDelete = dossierTypeRepository.findAll().collectList().block().size();

        // Delete the dossierType
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dossierType.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll().collectList().block();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
