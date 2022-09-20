package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import be.fgov.famhp.imt.backoffice.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDossierDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectorDossierMapper;
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
 * Integration tests for the {@link InspectorDossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspectorDossierResourceIT {

    private static final String ENTITY_API_URL = "/api/inspector-dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectorDossierRepository inspectorDossierRepository;

    @Autowired
    private InspectorDossierMapper inspectorDossierMapper;

    @Autowired
    private MockMvc restInspectorDossierMockMvc;

    private InspectorDossier inspectorDossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectorDossier createEntity() {
        InspectorDossier inspectorDossier = new InspectorDossier();
        return inspectorDossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectorDossier createUpdatedEntity() {
        InspectorDossier inspectorDossier = new InspectorDossier();
        return inspectorDossier;
    }

    @BeforeEach
    public void initTest() {
        inspectorDossierRepository.deleteAll();
        inspectorDossier = createEntity();
    }

    @Test
    void createInspectorDossier() throws Exception {
        int databaseSizeBeforeCreate = inspectorDossierRepository.findAll().size();
        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);
        restInspectorDossierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeCreate + 1);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void createInspectorDossierWithExistingId() throws Exception {
        // Create the InspectorDossier with an existing ID
        inspectorDossier.setId("existing_id");
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        int databaseSizeBeforeCreate = inspectorDossierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectorDossierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectorDossiers() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        // Get all the inspectorDossierList
        restInspectorDossierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspectorDossier.getId())));
    }

    @Test
    void getInspectorDossier() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        // Get the inspectorDossier
        restInspectorDossierMockMvc
            .perform(get(ENTITY_API_URL_ID, inspectorDossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspectorDossier.getId()));
    }

    @Test
    void getNonExistingInspectorDossier() throws Exception {
        // Get the inspectorDossier
        restInspectorDossierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInspectorDossier() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();

        // Update the inspectorDossier
        InspectorDossier updatedInspectorDossier = inspectorDossierRepository.findById(inspectorDossier.getId()).get();
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(updatedInspectorDossier);

        restInspectorDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isOk());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void putNonExistingInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectorDossierWithPatch() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();

        // Update the inspectorDossier using partial update
        InspectorDossier partialUpdatedInspectorDossier = new InspectorDossier();
        partialUpdatedInspectorDossier.setId(inspectorDossier.getId());

        restInspectorDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectorDossier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectorDossier))
            )
            .andExpect(status().isOk());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void fullUpdateInspectorDossierWithPatch() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();

        // Update the inspectorDossier using partial update
        InspectorDossier partialUpdatedInspectorDossier = new InspectorDossier();
        partialUpdatedInspectorDossier.setId(inspectorDossier.getId());

        restInspectorDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectorDossier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectorDossier))
            )
            .andExpect(status().isOk());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
        InspectorDossier testInspectorDossier = inspectorDossierList.get(inspectorDossierList.size() - 1);
    }

    @Test
    void patchNonExistingInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspectorDossierDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectorDossier() throws Exception {
        int databaseSizeBeforeUpdate = inspectorDossierRepository.findAll().size();
        inspectorDossier.setId(UUID.randomUUID().toString());

        // Create the InspectorDossier
        InspectorDossierDTO inspectorDossierDTO = inspectorDossierMapper.toDto(inspectorDossier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorDossierMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDossierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectorDossier in the database
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectorDossier() throws Exception {
        // Initialize the database
        inspectorDossierRepository.save(inspectorDossier);

        int databaseSizeBeforeDelete = inspectorDossierRepository.findAll().size();

        // Delete the inspectorDossier
        restInspectorDossierMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspectorDossier.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InspectorDossier> inspectorDossierList = inspectorDossierRepository.findAll();
        assertThat(inspectorDossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
