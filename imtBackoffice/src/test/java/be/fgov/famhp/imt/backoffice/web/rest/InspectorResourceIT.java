package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Inspector;
import be.fgov.famhp.imt.backoffice.repository.InspectorRepository;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectorMapper;
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
 * Integration tests for the {@link InspectorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspectorResourceIT {

    private static final String ENTITY_API_URL = "/api/inspectors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectorRepository inspectorRepository;

    @Autowired
    private InspectorMapper inspectorMapper;

    @Autowired
    private MockMvc restInspectorMockMvc;

    private Inspector inspector;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspector createEntity() {
        Inspector inspector = new Inspector();
        return inspector;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inspector createUpdatedEntity() {
        Inspector inspector = new Inspector();
        return inspector;
    }

    @BeforeEach
    public void initTest() {
        inspectorRepository.deleteAll();
        inspector = createEntity();
    }

    @Test
    void createInspector() throws Exception {
        int databaseSizeBeforeCreate = inspectorRepository.findAll().size();
        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);
        restInspectorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeCreate + 1);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void createInspectorWithExistingId() throws Exception {
        // Create the Inspector with an existing ID
        inspector.setId("existing_id");
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        int databaseSizeBeforeCreate = inspectorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectors() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        // Get all the inspectorList
        restInspectorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspector.getId())));
    }

    @Test
    void getInspector() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        // Get the inspector
        restInspectorMockMvc
            .perform(get(ENTITY_API_URL_ID, inspector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspector.getId()));
    }

    @Test
    void getNonExistingInspector() throws Exception {
        // Get the inspector
        restInspectorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInspector() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();

        // Update the inspector
        Inspector updatedInspector = inspectorRepository.findById(inspector.getId()).get();
        InspectorDTO inspectorDTO = inspectorMapper.toDto(updatedInspector);

        restInspectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void putNonExistingInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectorWithPatch() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();

        // Update the inspector using partial update
        Inspector partialUpdatedInspector = new Inspector();
        partialUpdatedInspector.setId(inspector.getId());

        restInspectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspector.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspector))
            )
            .andExpect(status().isOk());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void fullUpdateInspectorWithPatch() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();

        // Update the inspector using partial update
        Inspector partialUpdatedInspector = new Inspector();
        partialUpdatedInspector.setId(inspector.getId());

        restInspectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspector.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspector))
            )
            .andExpect(status().isOk());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
        Inspector testInspector = inspectorList.get(inspectorList.size() - 1);
    }

    @Test
    void patchNonExistingInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspectorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspector() throws Exception {
        int databaseSizeBeforeUpdate = inspectorRepository.findAll().size();
        inspector.setId(UUID.randomUUID().toString());

        // Create the Inspector
        InspectorDTO inspectorDTO = inspectorMapper.toDto(inspector);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Inspector in the database
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspector() throws Exception {
        // Initialize the database
        inspectorRepository.save(inspector);

        int databaseSizeBeforeDelete = inspectorRepository.findAll().size();

        // Delete the inspector
        restInspectorMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspector.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Inspector> inspectorList = inspectorRepository.findAll();
        assertThat(inspectorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
