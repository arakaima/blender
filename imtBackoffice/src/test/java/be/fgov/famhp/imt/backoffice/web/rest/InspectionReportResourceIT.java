package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import be.fgov.famhp.imt.backoffice.repository.InspectionReportRepository;
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
 * Integration tests for the {@link InspectionReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspectionReportResourceIT {

    private static final String ENTITY_API_URL = "/api/inspection-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionReportRepository inspectionReportRepository;

    @Autowired
    private MockMvc restInspectionReportMockMvc;

    private InspectionReport inspectionReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionReport createEntity() {
        InspectionReport inspectionReport = new InspectionReport();
        return inspectionReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionReport createUpdatedEntity() {
        InspectionReport inspectionReport = new InspectionReport();
        return inspectionReport;
    }

    @BeforeEach
    public void initTest() {
        inspectionReportRepository.deleteAll();
        inspectionReport = createEntity();
    }

    @Test
    void createInspectionReport() throws Exception {
        int databaseSizeBeforeCreate = inspectionReportRepository.findAll().size();
        // Create the InspectionReport
        restInspectionReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isCreated());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeCreate + 1);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void createInspectionReportWithExistingId() throws Exception {
        // Create the InspectionReport with an existing ID
        inspectionReport.setId("existing_id");

        int databaseSizeBeforeCreate = inspectionReportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionReportMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectionReports() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        // Get all the inspectionReportList
        restInspectionReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspectionReport.getId())));
    }

    @Test
    void getInspectionReport() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        // Get the inspectionReport
        restInspectionReportMockMvc
            .perform(get(ENTITY_API_URL_ID, inspectionReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspectionReport.getId()));
    }

    @Test
    void getNonExistingInspectionReport() throws Exception {
        // Get the inspectionReport
        restInspectionReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInspectionReport() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();

        // Update the inspectionReport
        InspectionReport updatedInspectionReport = inspectionReportRepository.findById(inspectionReport.getId()).get();

        restInspectionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspectionReport.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInspectionReport))
            )
            .andExpect(status().isOk());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void putNonExistingInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectionReport.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectionReportWithPatch() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();

        // Update the inspectionReport using partial update
        InspectionReport partialUpdatedInspectionReport = new InspectionReport();
        partialUpdatedInspectionReport.setId(inspectionReport.getId());

        restInspectionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectionReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionReport))
            )
            .andExpect(status().isOk());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void fullUpdateInspectionReportWithPatch() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();

        // Update the inspectionReport using partial update
        InspectionReport partialUpdatedInspectionReport = new InspectionReport();
        partialUpdatedInspectionReport.setId(inspectionReport.getId());

        restInspectionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectionReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionReport))
            )
            .andExpect(status().isOk());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
        InspectionReport testInspectionReport = inspectionReportList.get(inspectionReportList.size() - 1);
    }

    @Test
    void patchNonExistingInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspectionReport.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectionReport() throws Exception {
        int databaseSizeBeforeUpdate = inspectionReportRepository.findAll().size();
        inspectionReport.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionReportMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionReport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectionReport in the database
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectionReport() throws Exception {
        // Initialize the database
        inspectionReportRepository.save(inspectionReport);

        int databaseSizeBeforeDelete = inspectionReportRepository.findAll().size();

        // Delete the inspectionReport
        restInspectionReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspectionReport.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InspectionReport> inspectionReportList = inspectionReportRepository.findAll();
        assertThat(inspectionReportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
