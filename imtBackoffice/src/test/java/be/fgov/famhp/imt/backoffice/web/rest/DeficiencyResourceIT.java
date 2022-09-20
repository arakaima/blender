package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Deficiency;
import be.fgov.famhp.imt.backoffice.repository.DeficiencyRepository;
import be.fgov.famhp.imt.backoffice.service.dto.DeficiencyDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DeficiencyMapper;
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
 * Integration tests for the {@link DeficiencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeficiencyResourceIT {

    private static final String ENTITY_API_URL = "/api/deficiencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DeficiencyRepository deficiencyRepository;

    @Autowired
    private DeficiencyMapper deficiencyMapper;

    @Autowired
    private MockMvc restDeficiencyMockMvc;

    private Deficiency deficiency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deficiency createEntity() {
        Deficiency deficiency = new Deficiency();
        return deficiency;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deficiency createUpdatedEntity() {
        Deficiency deficiency = new Deficiency();
        return deficiency;
    }

    @BeforeEach
    public void initTest() {
        deficiencyRepository.deleteAll();
        deficiency = createEntity();
    }

    @Test
    void createDeficiency() throws Exception {
        int databaseSizeBeforeCreate = deficiencyRepository.findAll().size();
        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);
        restDeficiencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeCreate + 1);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void createDeficiencyWithExistingId() throws Exception {
        // Create the Deficiency with an existing ID
        deficiency.setId("existing_id");
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        int databaseSizeBeforeCreate = deficiencyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeficiencyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDeficiencies() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        // Get all the deficiencyList
        restDeficiencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deficiency.getId())));
    }

    @Test
    void getDeficiency() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        // Get the deficiency
        restDeficiencyMockMvc
            .perform(get(ENTITY_API_URL_ID, deficiency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deficiency.getId()));
    }

    @Test
    void getNonExistingDeficiency() throws Exception {
        // Get the deficiency
        restDeficiencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDeficiency() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();

        // Update the deficiency
        Deficiency updatedDeficiency = deficiencyRepository.findById(deficiency.getId()).get();
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(updatedDeficiency);

        restDeficiencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deficiencyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void putNonExistingDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deficiencyDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDeficiencyWithPatch() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();

        // Update the deficiency using partial update
        Deficiency partialUpdatedDeficiency = new Deficiency();
        partialUpdatedDeficiency.setId(deficiency.getId());

        restDeficiencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeficiency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeficiency))
            )
            .andExpect(status().isOk());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void fullUpdateDeficiencyWithPatch() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();

        // Update the deficiency using partial update
        Deficiency partialUpdatedDeficiency = new Deficiency();
        partialUpdatedDeficiency.setId(deficiency.getId());

        restDeficiencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeficiency.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeficiency))
            )
            .andExpect(status().isOk());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
        Deficiency testDeficiency = deficiencyList.get(deficiencyList.size() - 1);
    }

    @Test
    void patchNonExistingDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deficiencyDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDeficiency() throws Exception {
        int databaseSizeBeforeUpdate = deficiencyRepository.findAll().size();
        deficiency.setId(UUID.randomUUID().toString());

        // Create the Deficiency
        DeficiencyDTO deficiencyDTO = deficiencyMapper.toDto(deficiency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeficiencyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(deficiencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deficiency in the database
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDeficiency() throws Exception {
        // Initialize the database
        deficiencyRepository.save(deficiency);

        int databaseSizeBeforeDelete = deficiencyRepository.findAll().size();

        // Delete the deficiency
        restDeficiencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, deficiency.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Deficiency> deficiencyList = deficiencyRepository.findAll();
        assertThat(deficiencyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
