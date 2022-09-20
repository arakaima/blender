package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import be.fgov.famhp.imt.backoffice.repository.InspectionTypeRepository;
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

    private static final String ENTITY_API_URL = "/api/inspection-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InspectionTypeRepository inspectionTypeRepository;

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
        InspectionType inspectionType = new InspectionType()
            .nameFr(DEFAULT_NAME_FR)
            .nameEn(DEFAULT_NAME_EN)
            .nameNl(DEFAULT_NAME_NL)
            .nameDe(DEFAULT_NAME_DE)
            .deprecated(DEFAULT_DEPRECATED);
        return inspectionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InspectionType createUpdatedEntity() {
        InspectionType inspectionType = new InspectionType()
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);
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
        restInspectionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
            )
            .andExpect(status().isCreated());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeCreate + 1);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
        assertThat(testInspectionType.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testInspectionType.getNameEn()).isEqualTo(DEFAULT_NAME_EN);
        assertThat(testInspectionType.getNameNl()).isEqualTo(DEFAULT_NAME_NL);
        assertThat(testInspectionType.getNameDe()).isEqualTo(DEFAULT_NAME_DE);
        assertThat(testInspectionType.getDeprecated()).isEqualTo(DEFAULT_DEPRECATED);
    }

    @Test
    void createInspectionTypeWithExistingId() throws Exception {
        // Create the InspectionType with an existing ID
        inspectionType.setId("existing_id");

        int databaseSizeBeforeCreate = inspectionTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInspectionTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(inspectionType.getId())))
            .andExpect(jsonPath("$.[*].nameFr").value(hasItem(DEFAULT_NAME_FR)))
            .andExpect(jsonPath("$.[*].nameEn").value(hasItem(DEFAULT_NAME_EN)))
            .andExpect(jsonPath("$.[*].nameNl").value(hasItem(DEFAULT_NAME_NL)))
            .andExpect(jsonPath("$.[*].nameDe").value(hasItem(DEFAULT_NAME_DE)))
            .andExpect(jsonPath("$.[*].deprecated").value(hasItem(DEFAULT_DEPRECATED.booleanValue())));
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
            .andExpect(jsonPath("$.id").value(inspectionType.getId()))
            .andExpect(jsonPath("$.nameFr").value(DEFAULT_NAME_FR))
            .andExpect(jsonPath("$.nameEn").value(DEFAULT_NAME_EN))
            .andExpect(jsonPath("$.nameNl").value(DEFAULT_NAME_NL))
            .andExpect(jsonPath("$.nameDe").value(DEFAULT_NAME_DE))
            .andExpect(jsonPath("$.deprecated").value(DEFAULT_DEPRECATED.booleanValue()));
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
        updatedInspectionType
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInspectionType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInspectionType))
            )
            .andExpect(status().isOk());

        // Validate the InspectionType in the database
        List<InspectionType> inspectionTypeList = inspectionTypeRepository.findAll();
        assertThat(inspectionTypeList).hasSize(databaseSizeBeforeUpdate);
        InspectionType testInspectionType = inspectionTypeList.get(inspectionTypeList.size() - 1);
        assertThat(testInspectionType.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testInspectionType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testInspectionType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testInspectionType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testInspectionType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void putNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inspectionType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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

        partialUpdatedInspectionType.nameEn(UPDATED_NAME_EN).nameNl(UPDATED_NAME_NL).nameDe(UPDATED_NAME_DE).deprecated(UPDATED_DEPRECATED);

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
        assertThat(testInspectionType.getNameFr()).isEqualTo(DEFAULT_NAME_FR);
        assertThat(testInspectionType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testInspectionType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testInspectionType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testInspectionType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void fullUpdateInspectionTypeWithPatch() throws Exception {
        // Initialize the database
        inspectionTypeRepository.save(inspectionType);

        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();

        // Update the inspectionType using partial update
        InspectionType partialUpdatedInspectionType = new InspectionType();
        partialUpdatedInspectionType.setId(inspectionType.getId());

        partialUpdatedInspectionType
            .nameFr(UPDATED_NAME_FR)
            .nameEn(UPDATED_NAME_EN)
            .nameNl(UPDATED_NAME_NL)
            .nameDe(UPDATED_NAME_DE)
            .deprecated(UPDATED_DEPRECATED);

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
        assertThat(testInspectionType.getNameFr()).isEqualTo(UPDATED_NAME_FR);
        assertThat(testInspectionType.getNameEn()).isEqualTo(UPDATED_NAME_EN);
        assertThat(testInspectionType.getNameNl()).isEqualTo(UPDATED_NAME_NL);
        assertThat(testInspectionType.getNameDe()).isEqualTo(UPDATED_NAME_DE);
        assertThat(testInspectionType.getDeprecated()).isEqualTo(UPDATED_DEPRECATED);
    }

    @Test
    void patchNonExistingInspectionType() throws Exception {
        int databaseSizeBeforeUpdate = inspectionTypeRepository.findAll().size();
        inspectionType.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inspectionType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInspectionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(inspectionType))
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
