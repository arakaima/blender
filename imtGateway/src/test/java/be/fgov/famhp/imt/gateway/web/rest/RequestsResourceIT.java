package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Requests;
import be.fgov.famhp.imt.gateway.repository.RequestsRepository;
import be.fgov.famhp.imt.gateway.service.dto.RequestsDTO;
import be.fgov.famhp.imt.gateway.service.mapper.RequestsMapper;
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
 * Integration tests for the {@link RequestsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RequestsResourceIT {

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private RequestsMapper requestsMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Requests requests;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requests createEntity() {
        Requests requests = new Requests();
        return requests;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requests createUpdatedEntity() {
        Requests requests = new Requests();
        return requests;
    }

    @BeforeEach
    public void initTest() {
        requestsRepository.deleteAll().block();
        requests = createEntity();
    }

    @Test
    void createRequests() throws Exception {
        int databaseSizeBeforeCreate = requestsRepository.findAll().collectList().block().size();
        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeCreate + 1);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void createRequestsWithExistingId() throws Exception {
        // Create the Requests with an existing ID
        requests.setId("existing_id");
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        int databaseSizeBeforeCreate = requestsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequestsAsStream() {
        // Initialize the database
        requestsRepository.save(requests).block();

        List<Requests> requestsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RequestsDTO.class)
            .getResponseBody()
            .map(requestsMapper::toEntity)
            .filter(requests::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(requestsList).isNotNull();
        assertThat(requestsList).hasSize(1);
        Requests testRequests = requestsList.get(0);
    }

    @Test
    void getAllRequests() {
        // Initialize the database
        requestsRepository.save(requests).block();

        // Get all the requestsList
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
            .value(hasItem(requests.getId()));
    }

    @Test
    void getRequests() {
        // Initialize the database
        requestsRepository.save(requests).block();

        // Get the requests
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, requests.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(requests.getId()));
    }

    @Test
    void getNonExistingRequests() {
        // Get the requests
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRequests() throws Exception {
        // Initialize the database
        requestsRepository.save(requests).block();

        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();

        // Update the requests
        Requests updatedRequests = requestsRepository.findById(requests.getId()).block();
        RequestsDTO requestsDTO = requestsMapper.toDto(updatedRequests);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void putNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestsDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestsWithPatch() throws Exception {
        // Initialize the database
        requestsRepository.save(requests).block();

        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();

        // Update the requests using partial update
        Requests partialUpdatedRequests = new Requests();
        partialUpdatedRequests.setId(requests.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequests.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequests))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void fullUpdateRequestsWithPatch() throws Exception {
        // Initialize the database
        requestsRepository.save(requests).block();

        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();

        // Update the requests using partial update
        Requests partialUpdatedRequests = new Requests();
        partialUpdatedRequests.setId(requests.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequests.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequests))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void patchNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requestsDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().collectList().block().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequests() {
        // Initialize the database
        requestsRepository.save(requests).block();

        int databaseSizeBeforeDelete = requestsRepository.findAll().collectList().block().size();

        // Delete the requests
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, requests.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Requests> requestsList = requestsRepository.findAll().collectList().block();
        assertThat(requestsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
