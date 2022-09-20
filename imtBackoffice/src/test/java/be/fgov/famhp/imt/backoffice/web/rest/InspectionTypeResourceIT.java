package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import be.fgov.famhp.imt.backoffice.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionTypeDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectionTypeMapper;
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
 * Integration tests for the {@link InspectionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InspectionTypeResourceIT {

    private static final String ENTITY_API_URL = "/api/inspection-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionTypeRepository inspectionTypeRepository;

    @Autowired
    private InspectionTypeMapper inspectionTypeMapper;

    @Autowired
    private MockMvc restInspectionTypeMockMvc;

    private InspectionType inspectionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionType createEntity() {
        InspectionType inspectionType = new InspectionType();
        return inspectionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionType createUpdatedEntity() {
        InspectionType inspectionType = new InspectionType();
        return inspectionType;
    }

    @BeforeEach
    public void initTest() {
        inspectionTypeRepository.deleteAll();
        inspectionType = createEntity();
    }

    @Test
    void createInspectionType() throws Exception {
        int databaseSizeBeforeCreate = inspectionTypeRepository.findAll().size();
        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);
        restInspectionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void createInspectionTypeWithExistingId() throws Exception {
        // Create the InspectionType with an existing ID
        inspectionType.setId("existing_id");
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        int databaseSizeBeforeCreate = inspectionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInspectionTypes() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        // Get all the inspectionTypeList
        restInspectionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspectionType.getId())));
    }

    @Test
    void getInspectionType() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        // Get the inspectionType
        restInspectionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, inspectionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inspectionType.getId()));
    }

    @Test
    void getNonExistingInspectionType() throws Exception {
        // Get the inspectionType
        restInspectionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInspectionType() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();

        // Update the inspectionType
        InspectionType updatedInspectionType = inspectionTypeRepository.findById(inspectionType.getId()).get();
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(updatedInspectionType);

        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void putNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInspectionTypeWithPatch() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();

        // Update the inspectionType using partial update
        InspectionType partialUpdatedInspectionType = new InspectionType();
        partialUpdatedInspectionType.setId(inspectionType.getId());

        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionType))
            )
            .andExpect(status().isOk());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void fullUpdateInspectionTypeWithPatch() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();

        // Update the inspectionType using partial update
        InspectionType partialUpdatedInspectionType = new InspectionType();
        partialUpdatedInspectionType.setId(inspectionType.getId());

        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInspectionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInspectionType))
            )
            .andExpect(status().isOk());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
    }

    @Test
    void patchNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspectionTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // Create the InspectionType
        InspectionTypeDTO inspectionTypeDTO = inspectionTypeMapper.toDto(inspectionType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInspectionType() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        int databaseSizeBeforeDelete = inspectionTypeRepository.findAll().size();

        // Delete the inspectionType
        restInspectionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, inspectionType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
