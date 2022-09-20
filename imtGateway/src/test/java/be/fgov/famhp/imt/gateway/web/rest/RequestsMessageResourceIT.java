package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.RequestsMessage;
import be.fgov.famhp.imt.gateway.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.gateway.service.dto.RequestsMessageDTO;
import be.fgov.famhp.imt.gateway.service.mapper.RequestsMessageMapper;
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
 * Integration tests for the {@link RequestsMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class RequestsMessageResourceIT {

    private static final String ENTITY_API_URL = "/api/requests-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestsMessageRepository requestsMessageRepository;

    @Autowired
    private RequestsMessageMapper requestsMessageMapper;

    @Autowired
    private WebTestClient webTestClient;

    private RequestsMessage requestsMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestsMessage createEntity() {
        RequestsMessage requestsMessage = new RequestsMessage();
        return requestsMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequestsMessage createUpdatedEntity() {
        RequestsMessage requestsMessage = new RequestsMessage();
        return requestsMessage;
    }

    @BeforeEach
    public void initTest() {
        requestsMessageRepository.deleteAll().block();
        requestsMessage = createEntity();
    }

    @Test
    void createRequestsMessage() throws Exception {
        int databaseSizeBeforeCreate = requestsMessageRepository.findAll().collectList().block().size();
        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeCreate + 1);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void createRequestsMessageWithExistingId() throws Exception {
        // Create the RequestsMessage with an existing ID
        requestsMessage.setId("existing_id");
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        int databaseSizeBeforeCreate = requestsMessageRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequestsMessagesAsStream() {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        List<RequestsMessage> requestsMessageList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(RequestsMessageDTO.class)
            .getResponseBody()
            .map(requestsMessageMapper::toEntity)
            .filter(requestsMessage::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(requestsMessageList).isNotNull();
        assertThat(requestsMessageList).hasSize(1);
        RequestsMessage testRequestsMessage = requestsMessageList.get(0);
    }

    @Test
    void getAllRequestsMessages() {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        // Get all the requestsMessageList
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
            .value(hasItem(requestsMessage.getId()));
    }

    @Test
    void getRequestsMessage() {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        // Get the requestsMessage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, requestsMessage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(requestsMessage.getId()));
    }

    @Test
    void getNonExistingRequestsMessage() {
        // Get the requestsMessage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingRequestsMessage() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();

        // Update the requestsMessage
        RequestsMessage updatedRequestsMessage = requestsMessageRepository.findById(requestsMessage.getId()).block();
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(updatedRequestsMessage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestsMessageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void putNonExistingRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, requestsMessageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestsMessageWithPatch() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();

        // Update the requestsMessage using partial update
        RequestsMessage partialUpdatedRequestsMessage = new RequestsMessage();
        partialUpdatedRequestsMessage.setId(requestsMessage.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestsMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestsMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void fullUpdateRequestsMessageWithPatch() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();

        // Update the requestsMessage using partial update
        RequestsMessage partialUpdatedRequestsMessage = new RequestsMessage();
        partialUpdatedRequestsMessage.setId(requestsMessage.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedRequestsMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestsMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void patchNonExistingRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, requestsMessageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().collectList().block().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequestsMessage() {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage).block();

        int databaseSizeBeforeDelete = requestsMessageRepository.findAll().collectList().block().size();

        // Delete the requestsMessage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, requestsMessage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll().collectList().block();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
