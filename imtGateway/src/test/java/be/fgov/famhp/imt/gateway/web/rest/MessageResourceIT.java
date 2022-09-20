package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Message;
import be.fgov.famhp.imt.gateway.repository.MessageRepository;
import be.fgov.famhp.imt.gateway.service.dto.MessageDTO;
import be.fgov.famhp.imt.gateway.service.mapper.MessageMapper;
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
 * Integration tests for the {@link MessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class MessageResourceIT {

    private static final String ENTITY_API_URL = "/api/messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Message message;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity() {
        Message message = new Message();
        return message;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createUpdatedEntity() {
        Message message = new Message();
        return message;
    }

    @BeforeEach
    public void initTest() {
        messageRepository.deleteAll().block();
        message = createEntity();
    }

    @Test
    void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().collectList().block().size();
        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
    }

    @Test
    void createMessageWithExistingId() throws Exception {
        // Create the Message with an existing ID
        message.setId("existing_id");
        MessageDTO messageDTO = messageMapper.toDto(message);

        int databaseSizeBeforeCreate = messageRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMessagesAsStream() {
        // Initialize the database
        messageRepository.save(message).block();

        List<Message> messageList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(MessageDTO.class)
            .getResponseBody()
            .map(messageMapper::toEntity)
            .filter(message::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(messageList).isNotNull();
        assertThat(messageList).hasSize(1);
        Message testMessage = messageList.get(0);
    }

    @Test
    void getAllMessages() {
        // Initialize the database
        messageRepository.save(message).block();

        // Get all the messageList
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
            .value(hasItem(message.getId()));
    }

    @Test
    void getMessage() {
        // Initialize the database
        messageRepository.save(message).block();

        // Get the message
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, message.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(message.getId()));
    }

    @Test
    void getNonExistingMessage() {
        // Get the message
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingMessage() throws Exception {
        // Initialize the database
        messageRepository.save(message).block();

        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).block();
        MessageDTO messageDTO = messageMapper.toDto(updatedMessage);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
    }

    @Test
    void putNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.save(message).block();

        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
    }

    @Test
    void fullUpdateMessageWithPatch() throws Exception {
        // Initialize the database
        messageRepository.save(message).block();

        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();

        // Update the message using partial update
        Message partialUpdatedMessage = new Message();
        partialUpdatedMessage.setId(message.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedMessage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedMessage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
    }

    @Test
    void patchNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, messageDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().collectList().block().size();
        message.setId(UUID.randomUUID().toString());

        // Create the Message
        MessageDTO messageDTO = messageMapper.toDto(message);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(messageDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMessage() {
        // Initialize the database
        messageRepository.save(message).block();

        int databaseSizeBeforeDelete = messageRepository.findAll().collectList().block().size();

        // Delete the message
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, message.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Message> messageList = messageRepository.findAll().collectList().block();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
