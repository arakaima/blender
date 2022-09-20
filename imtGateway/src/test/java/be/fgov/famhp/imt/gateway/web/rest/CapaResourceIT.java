package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Capa;
import be.fgov.famhp.imt.gateway.repository.CapaRepository;
import be.fgov.famhp.imt.gateway.service.dto.CapaDTO;
import be.fgov.famhp.imt.gateway.service.mapper.CapaMapper;
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
 * Integration tests for the {@link CapaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CapaResourceIT {

    private static final String ENTITY_API_URL = "/api/capas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CapaRepository capaRepository;

    @Autowired
    private CapaMapper capaMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Capa capa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capa createEntity() {
        Capa capa = new Capa();
        return capa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capa createUpdatedEntity() {
        Capa capa = new Capa();
        return capa;
    }

    @BeforeEach
    public void initTest() {
        capaRepository.deleteAll().block();
        capa = createEntity();
    }

    @Test
    void createCapa() throws Exception {
        int databaseSizeBeforeCreate = capaRepository.findAll().collectList().block().size();
        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeCreate + 1);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void createCapaWithExistingId() throws Exception {
        // Create the Capa with an existing ID
        capa.setId("existing_id");
        CapaDTO capaDTO = capaMapper.toDto(capa);

        int databaseSizeBeforeCreate = capaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCapasAsStream() {
        // Initialize the database
        capaRepository.save(capa).block();

        List<Capa> capaList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CapaDTO.class)
            .getResponseBody()
            .map(capaMapper::toEntity)
            .filter(capa::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(capaList).isNotNull();
        assertThat(capaList).hasSize(1);
        Capa testCapa = capaList.get(0);
    }

    @Test
    void getAllCapas() {
        // Initialize the database
        capaRepository.save(capa).block();

        // Get all the capaList
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
            .value(hasItem(capa.getId()));
    }

    @Test
    void getCapa() {
        // Initialize the database
        capaRepository.save(capa).block();

        // Get the capa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, capa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(capa.getId()));
    }

    @Test
    void getNonExistingCapa() {
        // Get the capa
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCapa() throws Exception {
        // Initialize the database
        capaRepository.save(capa).block();

        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();

        // Update the capa
        Capa updatedCapa = capaRepository.findById(capa.getId()).block();
        CapaDTO capaDTO = capaMapper.toDto(updatedCapa);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, capaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void putNonExistingCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, capaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCapaWithPatch() throws Exception {
        // Initialize the database
        capaRepository.save(capa).block();

        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();

        // Update the capa using partial update
        Capa partialUpdatedCapa = new Capa();
        partialUpdatedCapa.setId(capa.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void fullUpdateCapaWithPatch() throws Exception {
        // Initialize the database
        capaRepository.save(capa).block();

        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();

        // Update the capa using partial update
        Capa partialUpdatedCapa = new Capa();
        partialUpdatedCapa.setId(capa.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCapa.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCapa))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void patchNonExistingCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, capaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().collectList().block().size();
        capa.setId(UUID.randomUUID().toString());

        // Create the Capa
        CapaDTO capaDTO = capaMapper.toDto(capa);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(capaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCapa() {
        // Initialize the database
        capaRepository.save(capa).block();

        int databaseSizeBeforeDelete = capaRepository.findAll().collectList().block().size();

        // Delete the capa
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, capa.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Capa> capaList = capaRepository.findAll().collectList().block();
        assertThat(capaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
