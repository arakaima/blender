package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Dossier;
import be.fgov.famhp.imt.backoffice.repository.DossierRepository;
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
 * Integration tests for the {@link DossierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierResourceIT {

    private static final String DEFAULT_DOSSIER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DOSSIER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOSSIER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOSSIER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DOSSIER_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_DOSSIER_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_INSPECTION_ENTITY = "AAAAAAAAAA";
    private static final String UPDATED_INSPECTION_ENTITY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dossiers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createEntity() {
        Dossier dossier = new Dossier()
            .dossierNumber(DEFAULT_DOSSIER_NUMBER)
            .description(DEFAULT_DESCRIPTION)
            .dossierType(DEFAULT_DOSSIER_TYPE)
            .dossierStatus(DEFAULT_DOSSIER_STATUS)
            .inspectionEntity(DEFAULT_INSPECTION_ENTITY);
        return dossier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity() {
        Dossier dossier = new Dossier()
            .dossierNumber(UPDATED_DOSSIER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .dossierType(UPDATED_DOSSIER_TYPE)
            .dossierStatus(UPDATED_DOSSIER_STATUS)
            .inspectionEntity(UPDATED_INSPECTION_ENTITY);
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossierRepository.deleteAll();
        dossier = createEntity();
    }

    @Test
    void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();
        // Create the Dossier
        restDossierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getDossierNumber()).isEqualTo(DEFAULT_DOSSIER_NUMBER);
        assertThat(testDossier.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDossier.getDossierType()).isEqualTo(DEFAULT_DOSSIER_TYPE);
        assertThat(testDossier.getDossierStatus()).isEqualTo(DEFAULT_DOSSIER_STATUS);
        assertThat(testDossier.getInspectionEntity()).isEqualTo(DEFAULT_INSPECTION_ENTITY);
    }

    @Test
    void createDossierWithExistingId() throws Exception {
        // Create the Dossier with an existing ID
        dossier.setId("existing_id");

        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossiers() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        // Get all the dossierList
        restDossierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId())))
            .andExpect(jsonPath("$.[*].dossierNumber").value(hasItem(DEFAULT_DOSSIER_NUMBER)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dossierType").value(hasItem(DEFAULT_DOSSIER_TYPE)))
            .andExpect(jsonPath("$.[*].dossierStatus").value(hasItem(DEFAULT_DOSSIER_STATUS)))
            .andExpect(jsonPath("$.[*].inspectionEntity").value(hasItem(DEFAULT_INSPECTION_ENTITY)));
    }

    @Test
    void getDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        // Get the dossier
        restDossierMockMvc
            .perform(get(ENTITY_API_URL_ID, dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossier.getId()))
            .andExpect(jsonPath("$.dossierNumber").value(DEFAULT_DOSSIER_NUMBER))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dossierType").value(DEFAULT_DOSSIER_TYPE))
            .andExpect(jsonPath("$.dossierStatus").value(DEFAULT_DOSSIER_STATUS))
            .andExpect(jsonPath("$.inspectionEntity").value(DEFAULT_INSPECTION_ENTITY));
    }

    @Test
    void getNonExistingDossier() throws Exception {
        // Get the dossier
        restDossierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).get();
        updatedDossier
            .dossierNumber(UPDATED_DOSSIER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .dossierType(UPDATED_DOSSIER_TYPE)
            .dossierStatus(UPDATED_DOSSIER_STATUS)
            .inspectionEntity(UPDATED_INSPECTION_ENTITY);

        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDossier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getDossierNumber()).isEqualTo(UPDATED_DOSSIER_NUMBER);
        assertThat(testDossier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDossier.getDossierType()).isEqualTo(UPDATED_DOSSIER_TYPE);
        assertThat(testDossier.getDossierStatus()).isEqualTo(UPDATED_DOSSIER_STATUS);
        assertThat(testDossier.getInspectionEntity()).isEqualTo(UPDATED_INSPECTION_ENTITY);
    }

    @Test
    void putNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossier.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier.dossierType(UPDATED_DOSSIER_TYPE).inspectionEntity(UPDATED_INSPECTION_ENTITY);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getDossierNumber()).isEqualTo(DEFAULT_DOSSIER_NUMBER);
        assertThat(testDossier.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDossier.getDossierType()).isEqualTo(UPDATED_DOSSIER_TYPE);
        assertThat(testDossier.getDossierStatus()).isEqualTo(DEFAULT_DOSSIER_STATUS);
        assertThat(testDossier.getInspectionEntity()).isEqualTo(UPDATED_INSPECTION_ENTITY);
    }

    @Test
    void fullUpdateDossierWithPatch() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier using partial update
        Dossier partialUpdatedDossier = new Dossier();
        partialUpdatedDossier.setId(dossier.getId());

        partialUpdatedDossier
            .dossierNumber(UPDATED_DOSSIER_NUMBER)
            .description(UPDATED_DESCRIPTION)
            .dossierType(UPDATED_DOSSIER_TYPE)
            .dossierStatus(UPDATED_DOSSIER_STATUS)
            .inspectionEntity(UPDATED_INSPECTION_ENTITY);

        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossier))
            )
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getDossierNumber()).isEqualTo(UPDATED_DOSSIER_NUMBER);
        assertThat(testDossier.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDossier.getDossierType()).isEqualTo(UPDATED_DOSSIER_TYPE);
        assertThat(testDossier.getDossierStatus()).isEqualTo(UPDATED_DOSSIER_STATUS);
        assertThat(testDossier.getInspectionEntity()).isEqualTo(UPDATED_INSPECTION_ENTITY);
    }

    @Test
    void patchNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossier.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();
        dossier.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossier))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.save(dossier);

        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Delete the dossier
        restDossierMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossier.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
