package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Deficiency;
import be.fgov.famhp.imt.gateway.repository.DeficiencyRepository;
import be.fgov.famhp.imt.gateway.service.dto.DeficiencyDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DeficiencyMapper;
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
 * Integration tests for the {@link DeficiencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeficiencyResourceIT {

    private static final String ENTITY_API_URL = "/api/deficiencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeficiencyRepository deficiencyRepository;

    @Autowired
    private DeficiencyMapper deficiencyMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Deficiency deficiency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deficiency createEntity() {
        Deficiency deficiency = new Deficiency();
        return deficiency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deficiency createUpdatedEntity() {
        Deficiency deficiency = new Deficiency();
        return deficiency;
    }

    @BeforeEach
    public void initTest() {
        deficiencyRepository.deleteAll().block();
        deficiency = createEntity();
    }

    @Test
    void createDeficiency() throws Exception {
        int databaseSizeBeforeCreate = deficiencyRepository.findAll().collectList().block().size();
        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeCreate + 1);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void createDeficiencyWithExistingId() throws Exception {
        // Create the Deficiency with an existing ID
        deficiency.setId("existing_id");
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        int databaseSizeBeforeCreate = deficiencyRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDeficienciesAsStream() {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        List<Deficiency> deficiencyList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(DeficiencyDTO.class)
            .getResponseBody()
            .map(deficiencyMapper::toEntity)
            .filter(deficiency::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(deficiencyList).isNotNull();
        assertThat(deficiencyList).hasSize(1);
        Deficiency testDeficiency = deficiencyList.get(0);
    }

    @Test
    void getAllDeficiencies() {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        // Get all the deficiencyList
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
            .value(hasItem(deficiency.getId()));
    }

    @Test
    void getDeficiency() {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        // Get the deficiency
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deficiency.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deficiency.getId()));
    }

    @Test
    void getNonExistingDeficiency() {
        // Get the deficiency
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDeficiency() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();

        // Update the deficiency
        Deficiency updatedDeficiency = deficiencyRepository.findById(deficiency.getId()).block();
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(updatedDeficiency);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deficiencyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void putNonExistingDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deficiencyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeficiencyWithPatch() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();

        // Update the deficiency using partial update
        Deficiency partialUpdatedDeficiency = new Deficiency();
        partialUpdatedDeficiency.setId(deficiency.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeficiency.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeficiency))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void fullUpdateDeficiencyWithPatch() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();

        // Update the deficiency using partial update
        Deficiency partialUpdatedDeficiency = new Deficiency();
        partialUpdatedDeficiency.setId(deficiency.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeficiency.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedDeficiency))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void patchNonExistingDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deficiencyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().collectList().block().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeficiency() {
        // Initialize the database
        deficiencyRepository.save(deficiency).block();

        int databaseSizeBeforeDelete = deficiencyRepository.findAll().collectList().block().size();

        // Delete the deficiency
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deficiency.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Deficiency> deficiencyList = deficiencyRepository.findAll().collectList().block();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
