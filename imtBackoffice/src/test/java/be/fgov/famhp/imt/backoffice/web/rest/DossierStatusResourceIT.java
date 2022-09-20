package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import be.fgov.famhp.imt.backoffice.repository.DossierStatusRepository;
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
 * Integration tests for the {@link DossierStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DossierStatusResourceIT {

    private static final String DEFAULT_NAME_FR = "AAAAAAAAAA";
    private static final String UPDATED_NAME_FR = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_EN = "AAAAAAAAAA";
    private static final String UPDATED_NAME_EN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_NL = "AAAAAAAAAA";
    private static final String UPDATED_NAME_NL = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_DE = "AAAAAAAAAA";
    private static final String UPDATED_NAME_DE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEPRECATED = false;
    private static final Boolean UPDATED_DEPRECATED = true;

    private static final String ENTITY_API_URL = "/api/dossier-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierStatusRepository dossierStatusRepository;

    @Autowired
    private MockMvc restDossierStatusMockMvc;

    private DossierStatus dossierStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierStatus createEntity() {
        DossierStatus dossierStatus = new DossierStatus()
            .nameFr(DEFAULT_NAME_FR)
            .nameEn(DEFAULT_NAME_EN)
            .nameNl(DEFAULT_NAME_NL)
            .nameDe(DEFAULT_NAME_DE)
            .deprecated(DEFAULT_DEPRECATED);
        return dossierStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierStatus createUpdatedEntity() {
        DossierStatus dossierStatus = new DossierStatus()
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);
        return dossierStatus;
    }

    @BeforeEach
    public void initTest() {
        dossierStatusRepository.deleteAll();
        dossierStatus = createEntity();
    }

    @Test
    void createDossierStatus() throws Exception {
        int databaseSizeBeforeCreate = dossierStatusRepository.findAll().size();
        // Create the DossierStatus
        restDossierStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isCreated());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeCreate + 1);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
        assertThat(testDossierStatus.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testDossierStatus.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testDossierStatus.getNameNl()).isEqualTo(DEFAULT_NAME_NL);
        assertThat(testDossierStatus.getNameDe()).isEqualTo(DEFAULT_NAME_DE);
        assertThat(testDossierStatus.getDeprecated()).isEqualTo(DEFAULT_DEPRECATED);
    }

    @Test
    void createDossierStatusWithExistingId() throws Exception {
        // Create the DossierStatus with an existing ID
        dossierStatus.setId("existing_id");

        int databaseSizeBeforeCreate = dossierStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierStatusMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllDossierStatuses() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        // Get all the dossierStatusList
        restDossierStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierStatus.getId())))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameNl").value(hasItem(DEFAULT_NAME_NL)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].deprecated").value(hasItem(DEFAULT_DEPRECATED.booleanValue())));
    }

    @Test
    void getDossierStatus() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        // Get the dossierStatus
        restDossierStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, dossierStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dossierStatus.getId()))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameNl").value(DEFAULT_NAME_NL))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.deprecated").value(DEFAULT_DEPRECATED.booleanValue()));
    }

    @Test
    void getNonExistingDossierStatus() throws Exception {
        // Get the dossierStatus
        restDossierStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingDossierStatus() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();

        // Update the dossierStatus
        DossierStatus updatedDossierStatus = dossierStatusRepository.findById(dossierStatus.getId()).get();
        updatedDossierStatus
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

        restDossierStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDossierStatus.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDossierStatus))
            )
            .andExpect(status().isOk());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
        assertThat(testDossierStatus.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testDossierStatus.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testDossierStatus.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierStatus.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testDossierStatus.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void putNonExistingDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierStatus.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateDossierStatusWithPatch() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();

        // Update the dossierStatus using partial update
        DossierStatus partialUpdatedDossierStatus = new DossierStatus();
        partialUpdatedDossierStatus.setId(dossierStatus.getId());

        partialUpdatedDossierStatus.nameEn(UPDATED_NAME_EN).nameNl(UPDATED_NAME_NL);

        restDossierStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierStatus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierStatus))
            )
            .andExpect(status().isOk());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
        assertThat(testDossierStatus.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testDossierStatus.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testDossierStatus.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierStatus.getNameDe()).isEqualTo(DEFAULT_NAME_DE);
        assertThat(testDossierStatus.getDeprecated()).isEqualTo(DEFAULT_DEPRECATED);
    }

    @Test
    void fullUpdateDossierStatusWithPatch() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();

        // Update the dossierStatus using partial update
        DossierStatus partialUpdatedDossierStatus = new DossierStatus();
        partialUpdatedDossierStatus.setId(dossierStatus.getId());

        partialUpdatedDossierStatus
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

        restDossierStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDossierStatus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDossierStatus))
            )
            .andExpect(status().isOk());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
        DossierStatus testDossierStatus = dossierStatusList.get(dossierStatusList.size() - 1);
        assertThat(testDossierStatus.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testDossierStatus.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testDossierStatus.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierStatus.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testDossierStatus.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void patchNonExistingDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossierStatus.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamDossierStatus() throws Exception {
        int databaseSizeBeforeUpdate = dossierStatusRepository.findAll().size();
        dossierStatus.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DossierStatus in the database
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteDossierStatus() throws Exception {
        // Initialize the database
        dossierStatusRepository.save(dossierStatus);

        int databaseSizeBeforeDelete = dossierStatusRepository.findAll().size();

        // Delete the dossierStatus
        restDossierStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, dossierStatus.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DossierStatus> dossierStatusList = dossierStatusRepository.findAll();
        assertThat(dossierStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
