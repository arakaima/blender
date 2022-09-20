package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import be.fgov.famhp.imt.backoffice.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.dto.OrganizationDocumentDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.OrganizationDocumentMapper;
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
 * Integration tests for the {@link OrganizationDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrganizationDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/organization-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrganizationDocumentRepository organizationDocumentRepository;

    @Autowired
    private OrganizationDocumentMapper organizationDocumentMapper;

    @Autowired
    private MockMvc restOrganizationDocumentMockMvc;

    private OrganizationDocument organizationDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationDocument createEntity() {
        OrganizationDocument organizationDocument = new OrganizationDocument();
        return organizationDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationDocument createUpdatedEntity() {
        OrganizationDocument organizationDocument = new OrganizationDocument();
        return organizationDocument;
    }

    @BeforeEach
    public void initTest() {
        organizationDocumentRepository.deleteAll();
        organizationDocument = createEntity();
    }

    @Test
    void createOrganizationDocument() throws Exception {
        int databaseSizeBeforeCreate = organizationDocumentRepository.findAll().size();
        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);
        restOrganizationDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void createOrganizationDocumentWithExistingId() throws Exception {
        // Create the OrganizationDocument with an existing ID
        organizationDocument.setId("existing_id");
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        int databaseSizeBeforeCreate = organizationDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrganizationDocuments() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        // Get all the organizationDocumentList
        restOrganizationDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationDocument.getId())));
    }

    @Test
    void getOrganizationDocument() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        // Get the organizationDocument
        restOrganizationDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, organizationDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(organizationDocument.getId()));
    }

    @Test
    void getNonExistingOrganizationDocument() throws Exception {
        // Get the organizationDocument
        restOrganizationDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingOrganizationDocument() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();

        // Update the organizationDocument
        OrganizationDocument updatedOrganizationDocument = organizationDocumentRepository.findById(organizationDocument.getId()).get();
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(updatedOrganizationDocument);

        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void putNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrganizationDocumentWithPatch() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();

        // Update the organizationDocument using partial update
        OrganizationDocument partialUpdatedOrganizationDocument = new OrganizationDocument();
        partialUpdatedOrganizationDocument.setId(organizationDocument.getId());

        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationDocument))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void fullUpdateOrganizationDocumentWithPatch() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();

        // Update the organizationDocument using partial update
        OrganizationDocument partialUpdatedOrganizationDocument = new OrganizationDocument();
        partialUpdatedOrganizationDocument.setId(organizationDocument.getId());

        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrganizationDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganizationDocument))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationDocumentDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // Create the OrganizationDocument
        OrganizationDocumentDTO organizationDocumentDTO = organizationDocumentMapper.toDto(organizationDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrganizationDocument() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        int databaseSizeBeforeDelete = organizationDocumentRepository.findAll().size();

        // Delete the organizationDocument
        restOrganizationDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, organizationDocument.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
