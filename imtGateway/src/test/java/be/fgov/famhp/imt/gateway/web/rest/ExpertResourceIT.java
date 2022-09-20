package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Expert;
import be.fgov.famhp.imt.gateway.repository.ExpertRepository;
import be.fgov.famhp.imt.gateway.service.dto.ExpertDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ExpertMapper;
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
 * Integration tests for the {@link ExpertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ExpertResourceIT {

    private static final String ENTITY_API_URL = "/api/experts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private ExpertMapper expertMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Expert expert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createEntity() {
        Expert expert = new Expert();
        return expert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expert createUpdatedEntity() {
        Expert expert = new Expert();
        return expert;
    }

    @BeforeEach
    public void initTest() {
        expertRepository.deleteAll().block();
        expert = createEntity();
    }

    @Test
    void createExpert() throws Exception {
        int databaseSizeBeforeCreate = expertRepository.findAll().collectList().block().size();
        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeCreate + 1);
        Expert testExpert = expertList.get(expertList.size() - 1);
    }

    @Test
    void createExpertWithExistingId() throws Exception {
        // Create the Expert with an existing ID
        expert.setId("existing_id");
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        int databaseSizeBeforeCreate = expertRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllExpertsAsStream() {
        // Initialize the database
        expertRepository.save(expert).block();

        List<Expert> expertList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ExpertDTO.class)
            .getResponseBody()
            .map(expertMapper::toEntity)
            .filter(expert::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(expertList).isNotNull();
        assertThat(expertList).hasSize(1);
        Expert testExpert = expertList.get(0);
    }

    @Test
    void getAllExperts() {
        // Initialize the database
        expertRepository.save(expert).block();

        // Get all the expertList
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
            .value(hasItem(expert.getId()));
    }

    @Test
    void getExpert() {
        // Initialize the database
        expertRepository.save(expert).block();

        // Get the expert
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, expert.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(expert.getId()));
    }

    @Test
    void getNonExistingExpert() {
        // Get the expert
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingExpert() throws Exception {
        // Initialize the database
        expertRepository.save(expert).block();

        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();

        // Update the expert
        Expert updatedExpert = expertRepository.findById(expert.getId()).block();
        ExpertDTO expertDTO = expertMapper.toDto(updatedExpert);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, expertDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
    }

    @Test
    void putNonExistingExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, expertDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        expertRepository.save(expert).block();

        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExpert))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
    }

    @Test
    void fullUpdateExpertWithPatch() throws Exception {
        // Initialize the database
        expertRepository.save(expert).block();

        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();

        // Update the expert using partial update
        Expert partialUpdatedExpert = new Expert();
        partialUpdatedExpert.setId(expert.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedExpert.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedExpert))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
        Expert testExpert = expertList.get(expertList.size() - 1);
    }

    @Test
    void patchNonExistingExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, expertDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamExpert() throws Exception {
        int databaseSizeBeforeUpdate = expertRepository.findAll().collectList().block().size();
        expert.setId(UUID.randomUUID().toString());

        // Create the Expert
        ExpertDTO expertDTO = expertMapper.toDto(expert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(expertDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Expert in the database
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteExpert() {
        // Initialize the database
        expertRepository.save(expert).block();

        int databaseSizeBeforeDelete = expertRepository.findAll().collectList().block().size();

        // Delete the expert
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, expert.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Expert> expertList = expertRepository.findAll().collectList().block();
        assertThat(expertList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
