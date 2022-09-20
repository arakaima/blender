package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import be.fgov.famhp.imt.backoffice.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsMessageDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.RequestsMessageMapper;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link RequestsMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestsMessageResourceIT {

    private static final String ENTITY_API_URL = "/api/requests-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestsMessageRepository requestsMessageRepository;

    @Autowired
    private RequestsMessageMapper requestsMessageMapper;

    @Autowired
    private MockMvc restRequestsMessageMockMvc;

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
        requestsMessageRepository.deleteAll();
        requestsMessage = createEntity();
    }

    @Test
    void createRequestsMessage() throws Exception {
        int databaseSizeBeforeCreate = requestsMessageRepository.findAll().size();
        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);
        restRequestsMessageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isCreated());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeCreate + 1);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void createRequestsMessageWithExistingId() throws Exception {
        // Create the RequestsMessage with an existing ID
        requestsMessage.setId("existing_id");
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        int databaseSizeBeforeCreate = requestsMessageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestsMessageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequestsMessages() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        // Get all the requestsMessageList
        restRequestsMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requestsMessage.getId())));
    }

    @Test
    void getRequestsMessage() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        // Get the requestsMessage
        restRequestsMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, requestsMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requestsMessage.getId()));
    }

    @Test
    void getNonExistingRequestsMessage() throws Exception {
        // Get the requestsMessage
        restRequestsMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRequestsMessage() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();

        // Update the requestsMessage
        RequestsMessage updatedRequestsMessage = requestsMessageRepository.findById(requestsMessage.getId()).get();
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(updatedRequestsMessage);

        restRequestsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestsMessageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isOk());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void putNonExistingRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestsMessageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestsMessageWithPatch() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();

        // Update the requestsMessage using partial update
        RequestsMessage partialUpdatedRequestsMessage = new RequestsMessage();
        partialUpdatedRequestsMessage.setId(requestsMessage.getId());

        restRequestsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestsMessage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestsMessage))
            )
            .andExpect(status().isOk());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void fullUpdateRequestsMessageWithPatch() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();

        // Update the requestsMessage using partial update
        RequestsMessage partialUpdatedRequestsMessage = new RequestsMessage();
        partialUpdatedRequestsMessage.setId(requestsMessage.getId());

        restRequestsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequestsMessage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequestsMessage))
            )
            .andExpect(status().isOk());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
        RequestsMessage testRequestsMessage = requestsMessageList.get(requestsMessageList.size() - 1);
    }

    @Test
    void patchNonExistingRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestsMessageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequestsMessage() throws Exception {
        int databaseSizeBeforeUpdate = requestsMessageRepository.findAll().size();
        requestsMessage.setId(UUID.randomUUID().toString());

        // Create the RequestsMessage
        RequestsMessageDTO requestsMessageDTO = requestsMessageMapper.toDto(requestsMessage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMessageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsMessageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RequestsMessage in the database
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequestsMessage() throws Exception {
        // Initialize the database
        requestsMessageRepository.save(requestsMessage);

        int databaseSizeBeforeDelete = requestsMessageRepository.findAll().size();

        // Delete the requestsMessage
        restRequestsMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, requestsMessage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequestsMessage> requestsMessageList = requestsMessageRepository.findAll();
        assertThat(requestsMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
