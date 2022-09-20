package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.RequestsMapper;
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
 * Integration tests for the {@link RequestsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequestsResourceIT {

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestsRepository requestsRepository;

    @Autowired
    private RequestsMapper requestsMapper;

    @Autowired
    private MockMvc restRequestsMockMvc;

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
        requestsRepository.deleteAll();
        requests = createEntity();
    }

    @Test
    void createRequests() throws Exception {
        int databaseSizeBeforeCreate = requestsRepository.findAll().size();
        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);
        restRequestsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeCreate + 1);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void createRequestsWithExistingId() throws Exception {
        // Create the Requests with an existing ID
        requests.setId("existing_id");
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        int databaseSizeBeforeCreate = requestsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRequests() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        // Get all the requestsList
        restRequestsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requests.getId())));
    }

    @Test
    void getRequests() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        // Get the requests
        restRequestsMockMvc
            .perform(get(ENTITY_API_URL_ID, requests.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requests.getId()));
    }

    @Test
    void getNonExistingRequests() throws Exception {
        // Get the requests
        restRequestsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRequests() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();

        // Update the requests
        Requests updatedRequests = requestsRepository.findById(requests.getId()).get();
        RequestsDTO requestsDTO = requestsMapper.toDto(updatedRequests);

        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void putNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requestsDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRequestsWithPatch() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();

        // Update the requests using partial update
        Requests partialUpdatedRequests = new Requests();
        partialUpdatedRequests.setId(requests.getId());

        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequests.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequests))
            )
            .andExpect(status().isOk());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void fullUpdateRequestsWithPatch() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();

        // Update the requests using partial update
        Requests partialUpdatedRequests = new Requests();
        partialUpdatedRequests.setId(requests.getId());

        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequests.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequests))
            )
            .andExpect(status().isOk());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
    }

    @Test
    void patchNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requestsDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // Create the Requests
        RequestsDTO requestsDTO = requestsMapper.toDto(requests);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requestsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRequests() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        int databaseSizeBeforeDelete = requestsRepository.findAll().size();

        // Delete the requests
        restRequestsMockMvc
            .perform(delete(ENTITY_API_URL_ID, requests.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
