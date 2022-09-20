package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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

    private static final String DEFAULT_INSPECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_INSPECTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RequestsRepository requestsRepository;

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
        Requests requests = new Requests()
            .inspectionId(DEFAULT_INSPECTION_ID)
            .title(DEFAULT_TITLE)
            .datetime(DEFAULT_DATETIME)
            .status(DEFAULT_STATUS);
        return requests;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requests createUpdatedEntity() {
        Requests requests = new Requests()
            .inspectionId(UPDATED_INSPECTION_ID)
            .title(UPDATED_TITLE)
            .datetime(UPDATED_DATETIME)
            .status(UPDATED_STATUS);
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
        restRequestsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requests))
            )
            .andExpect(status().isCreated());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeCreate + 1);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
        assertThat(testRequests.getInspectionId()).isEqualTo(DEFAULT_INSPECTION_ID);
        assertThat(testRequests.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testRequests.getDatetime()).isEqualTo(DEFAULT_DATETIME);
        assertThat(testRequests.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createRequestsWithExistingId() throws Exception {
        // Create the Requests with an existing ID
        requests.setId("existing_id");

        int databaseSizeBeforeCreate = requestsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequestsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(requests.getId())))
            .andExpect(jsonPath("$.[*].inspectionId").value(hasItem(DEFAULT_INSPECTION_ID)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].datetime").value(hasItem(DEFAULT_DATETIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
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
            .andExpect(jsonPath("$.id").value(requests.getId()))
            .andExpect(jsonPath("$.inspectionId").value(DEFAULT_INSPECTION_ID))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.datetime").value(DEFAULT_DATETIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
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
        updatedRequests.inspectionId(UPDATED_INSPECTION_ID).title(UPDATED_TITLE).datetime(UPDATED_DATETIME).status(UPDATED_STATUS);

        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequests.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequests))
            )
            .andExpect(status().isOk());

        // Validate the Requests in the database
        List<Requests> requestsList = requestsRepository.findAll();
        assertThat(requestsList).hasSize(databaseSizeBeforeUpdate);
        Requests testRequests = requestsList.get(requestsList.size() - 1);
        assertThat(testRequests.getInspectionId()).isEqualTo(UPDATED_INSPECTION_ID);
        assertThat(testRequests.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRequests.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testRequests.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requests.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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

        partialUpdatedRequests.inspectionId(UPDATED_INSPECTION_ID).title(UPDATED_TITLE).datetime(UPDATED_DATETIME);

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
        assertThat(testRequests.getInspectionId()).isEqualTo(UPDATED_INSPECTION_ID);
        assertThat(testRequests.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRequests.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testRequests.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void fullUpdateRequestsWithPatch() throws Exception {
        // Initialize the database
        requestsRepository.save(requests);

        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();

        // Update the requests using partial update
        Requests partialUpdatedRequests = new Requests();
        partialUpdatedRequests.setId(requests.getId());

        partialUpdatedRequests.inspectionId(UPDATED_INSPECTION_ID).title(UPDATED_TITLE).datetime(UPDATED_DATETIME).status(UPDATED_STATUS);

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
        assertThat(testRequests.getInspectionId()).isEqualTo(UPDATED_INSPECTION_ID);
        assertThat(testRequests.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testRequests.getDatetime()).isEqualTo(UPDATED_DATETIME);
        assertThat(testRequests.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingRequests() throws Exception {
        int databaseSizeBeforeUpdate = requestsRepository.findAll().size();
        requests.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requests.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequestsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requests))
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
