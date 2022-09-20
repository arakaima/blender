package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.DossierType;
import be.fgov.famhp.imt.backoffice.repository.DossierTypeRepository;
import be.fgov.famhp.imt.backoffice.service.dto.DossierTypeDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DossierTypeMapper;
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
 * Integration tests for the {@link DossierTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierTypeResourceIT {

    private static final String ENTITY_API_URL = "/api/dossier-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierTypeRepository dossierTypeRepository;

    @Autowired
    private DossierTypeMapper dossierTypeMapper;

    @Autowired
    private MockMvc restDossierTypeMockMvc;

    private DossierType dossierType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierType createEntity() {
        DossierType dossierType = new DossierType();
        return dossierType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierType createUpdatedEntity() {
        DossierType dossierType = new DossierType();
        return dossierType;
    }

    @BeforeEach
    public void initTest() {
        dossierTypeRepository.deleteAll();
        dossierType = createEntity();
    }

    @Test
    void createDossierType() throws Exception {
        int databaseSizeBeforeCreate = dossierTypeRepository.findAll().size();
        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);
        restDossierTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void createDossierTypeWithExistingId() throws Exception {
        // Create the DossierType with an existing ID
        dossierType.setId("existing_id");
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        int databaseSizeBeforeCreate = dossierTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossierTypes() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        // Get all the dossierTypeList
        restDossierTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierType.getId())));
    }

    @Test
    void getDossierType() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        // Get the dossierType
        restDossierTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, dossierType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossierType.getId()));
    }

    @Test
    void getNonExistingDossierType() throws Exception {
        // Get the dossierType
        restDossierTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDossierType() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();

        // Update the dossierType
        DossierType updatedDossierType = dossierTypeRepository.findById(dossierType.getId()).get();
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(updatedDossierType);

        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void putNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierTypeWithPatch() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();

        // Update the dossierType using partial update
        DossierType partialUpdatedDossierType = new DossierType();
        partialUpdatedDossierType.setId(dossierType.getId());

        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierType))
            )
            .andExpect(status().isOk());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void fullUpdateDossierTypeWithPatch() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();

        // Update the dossierType using partial update
        DossierType partialUpdatedDossierType = new DossierType();
        partialUpdatedDossierType.setId(dossierType.getId());

        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierType))
            )
            .andExpect(status().isOk());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
    }

    @Test
    void patchNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossierTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // Create the DossierType
        DossierTypeDTO dossierTypeDTO = dossierTypeMapper.toDto(dossierType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossierType() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        int databaseSizeBeforeDelete = dossierTypeRepository.findAll().size();

        // Delete the dossierType
        restDossierTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossierType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
