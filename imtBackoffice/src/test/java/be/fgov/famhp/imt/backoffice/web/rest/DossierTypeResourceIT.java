package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.DossierType;
import be.fgov.famhp.imt.backoffice.repository.DossierTypeRepository;
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

    private static final String ENTITY_API_URL = "/api/dossier-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private DossierTypeRepository dossierTypeRepository;

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
        DossierType dossierType = new DossierType()
            .nameFr(DEFAULT_NAME_FR)
            .nameEn(DEFAULT_NAME_EN)
            .nameNl(DEFAULT_NAME_NL)
            .nameDe(DEFAULT_NAME_DE)
            .deprecated(DEFAULT_DEPRECATED);
        return dossierType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DossierType createUpdatedEntity() {
        DossierType dossierType = new DossierType()
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);
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
        restDossierTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
            )
            .andExpect(status().isCreated());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeCreate + 1);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
        assertThat(testDossierType.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testDossierType.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testDossierType.getNameNl()).isEqualTo(DEFAULT_NAME_NL);
        assertThat(testDossierType.getNameDe()).isEqualTo(DEFAULT_NAME_DE);
        assertThat(testDossierType.getDeprecated()).isEqualTo(DEFAULT_DEPRECATED);
    }

    @Test
    void createDossierTypeWithExistingId() throws Exception {
        // Create the DossierType with an existing ID
        dossierType.setId("existing_id");

        int databaseSizeBeforeCreate = dossierTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierType.getId())))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameNl").value(hasItem(DEFAULT_NAME_NL)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].deprecated").value(hasItem(DEFAULT_DEPRECATED.booleanValue())));
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
            .andExpect(jsonPath("$.id").value(dossierType.getId()))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameNl").value(DEFAULT_NAME_NL))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.deprecated").value(DEFAULT_DEPRECATED.booleanValue()));
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
        updatedDossierType
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDossierType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDossierType))
            )
            .andExpect(status().isOk());

        // Validate the DossierType in the database
        List<DossierType> dossierTypeList = dossierTypeRepository.findAll();
        assertThat(dossierTypeList).hasSize(databaseSizeBeforeUpdate);
        DossierType testDossierType = dossierTypeList.get(dossierTypeList.size() - 1);
        assertThat(testDossierType.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testDossierType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testDossierType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testDossierType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void putNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dossierType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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

        partialUpdatedDossierType.nameFr(UPDATED_NAME_FR).nameNl(UPDATED_NAME_NL).nameDe(UPDATED_NAME_DE).deprecated(UPDATED_DEPRECATED);

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
        assertThat(testDossierType.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testDossierType.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testDossierType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testDossierType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void fullUpdateDossierTypeWithPatch() throws Exception {
        // Initialize the database
        dossierTypeRepository.save(dossierType);

        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();

        // Update the dossierType using partial update
        DossierType partialUpdatedDossierType = new DossierType();
        partialUpdatedDossierType.setId(dossierType.getId());

        partialUpdatedDossierType
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

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
        assertThat(testDossierType.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testDossierType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testDossierType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testDossierType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testDossierType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void patchNonExistingDossierType() throws Exception {
        int databaseSizeBeforeUpdate = dossierTypeRepository.findAll().size();
        dossierType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dossierType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDossierTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(dossierType))
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
