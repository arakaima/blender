package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Dossier;
import be.fgov.famhp.imt.gateway.repository.DossierRepository;
import be.fgov.famhp.imt.gateway.service.dto.DossierDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierMapper;
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
 * Integration tests for the {@link DossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DossierResourceIT {

    private static final String ENTITY_API_URL = "/api/dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierMapper dossierMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Dossier dossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createEntity() {
        Dossier dossier = new Dossier();
        return dossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity() {
        Dossier dossier = new Dossier();
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossierRepository.deleteAll().block();
        dossier = createEntity();
    }

    @Test
    void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().collectList().block().size();
        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
    }

    @Test
    void createDossierWithExistingId() throws Exception {
        // Create the Dossier with an existing ID
        dossier.setId("existing_id");
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        int databaseSizeBeforeCreate = dossierRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossiersAsStream() {
        // Initialize the database
        dossierRepository.save(dossier).block();

        List<Dossier> dossierList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DossierDTO.class)
            .getResponseBody()
            .map(dossierMapper::toEntity)
            .filter(dossier::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(dossierList).isNotNull();
        assertThat(dossierList).hasSize(1);
        Dossier testDossier = dossierList.get(0);
    }

    @Test
    void getAllDossiers() {
        // Initialize the database
        dossierRepository.save(dossier).block();

        // Get all the dossierList
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
            .value(hasItem(dossier.getId()));
    }

    @Test
    void getDossier() {
        // Initialize the database
        dossierRepository.save(dossier).block();

        // Get the dossier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, dossier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(dossier.getId()));
    }

    @Test
    void getNonExistingDossier() {
        // Get the dossier
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier).block();

        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).block();
        DossierDTO dossierDTO = dossierMapper.toDto(updatedDossier);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
    }

    @Test
    void putNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, dossierDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier).block();

        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
    }

    @Test
    void fullUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier).block();

        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
    }

    @Test
    void patchNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, dossierDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().collectList().block().size();
        dossier.setId(UUID.randomUUID().toString());

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(dossierDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossier() {
        // Initialize the database
        dossierRepository.save(dossier).block();

        int databaseSizeBeforeDelete = dossierRepository.findAll().collectList().block().size();

        // Delete the dossier
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, dossier.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Dossier> dossierList = dossierRepository.findAll().collectList().block();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
