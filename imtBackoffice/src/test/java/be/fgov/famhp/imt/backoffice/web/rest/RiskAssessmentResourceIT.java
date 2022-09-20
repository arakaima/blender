package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import be.fgov.famhp.imt.backoffice.repository.RiskAssessmentRepository;
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
 * Integration tests for the {@link RiskAssessmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RiskAssessmentResourceIT {

    private static final String ENTITY_API_URL = "/api/risk-assessments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private RiskAssessmentRepository riskAssessmentRepository;

    @Autowired
    private MockMvc restRiskAssessmentMockMvc;

    private RiskAssessment riskAssessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createEntity() {
        RiskAssessment riskAssessment = new RiskAssessment();
        return riskAssessment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createUpdatedEntity() {
        RiskAssessment riskAssessment = new RiskAssessment();
        return riskAssessment;
    }

    @BeforeEach
    public void initTest() {
        riskAssessmentRepository.deleteAll();
        riskAssessment = createEntity();
    }

    @Test
    void createRiskAssessment() throws Exception {
        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().size();
        // Create the RiskAssessment
        restRiskAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isCreated());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate + 1);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void createRiskAssessmentWithExistingId() throws Exception {
        // Create the RiskAssessment with an existing ID
        riskAssessment.setId("existing_id");

        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskAssessmentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllRiskAssessments() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        // Get all the riskAssessmentList
        restRiskAssessmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(riskAssessment.getId())));
    }

    @Test
    void getRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        // Get the riskAssessment
        restRiskAssessmentMockMvc
            .perform(get(ENTITY_API_URL_ID, riskAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(riskAssessment.getId()));
    }

    @Test
    void getNonExistingRiskAssessment() throws Exception {
        // Get the riskAssessment
        restRiskAssessmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();

        // Update the riskAssessment
        RiskAssessment updatedRiskAssessment = riskAssessmentRepository.findById(riskAssessment.getId()).get();

        restRiskAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRiskAssessment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRiskAssessment))
            )
            .andExpect(status().isOk());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void putNonExistingRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, riskAssessment.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateRiskAssessmentWithPatch() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();

        // Update the riskAssessment using partial update
        RiskAssessment partialUpdatedRiskAssessment = new RiskAssessment();
        partialUpdatedRiskAssessment.setId(riskAssessment.getId());

        restRiskAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRiskAssessment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRiskAssessment))
            )
            .andExpect(status().isOk());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void fullUpdateRiskAssessmentWithPatch() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();

        // Update the riskAssessment using partial update
        RiskAssessment partialUpdatedRiskAssessment = new RiskAssessment();
        partialUpdatedRiskAssessment.setId(riskAssessment.getId());

        restRiskAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRiskAssessment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRiskAssessment))
            )
            .andExpect(status().isOk());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
    }

    @Test
    void patchNonExistingRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, riskAssessment.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();
        riskAssessment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(riskAssessment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.save(riskAssessment);

        int databaseSizeBeforeDelete = riskAssessmentRepository.findAll().size();

        // Delete the riskAssessment
        restRiskAssessmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, riskAssessment.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
