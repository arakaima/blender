package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import be.fgov.famhp.imt.backoffice.repository.OrganizationDocumentRepository;
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

    private static final String DEFAULT_DOCUMENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/organization-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrganizationDocumentRepository organizationDocumentRepository;

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
        OrganizationDocument organizationDocument = new OrganizationDocument()
            .documentName(DEFAULT_DOCUMENT_NAME)
            .documentTitle(DEFAULT_DOCUMENT_TITLE)
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .status(DEFAULT_STATUS);
        return organizationDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationDocument createUpdatedEntity() {
        OrganizationDocument organizationDocument = new OrganizationDocument()
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .status(UPDATED_STATUS);
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
        restOrganizationDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
            )
            .andExpect(status().isCreated());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
        assertThat(testOrganizationDocument.getDocumentName()).isEqualTo(DEFAULT_DOCUMENT_NAME);
        assertThat(testOrganizationDocument.getDocumentTitle()).isEqualTo(DEFAULT_DOCUMENT_TITLE);
        assertThat(testOrganizationDocument.getDocumentType()).isEqualTo(DEFAULT_DOCUMENT_TYPE);
        assertThat(testOrganizationDocument.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    void createOrganizationDocumentWithExistingId() throws Exception {
        // Create the OrganizationDocument with an existing ID
        organizationDocument.setId("existing_id");

        int databaseSizeBeforeCreate = organizationDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationDocument.getId())))
            .andExpect(jsonPath("$.[*].documentName").value(hasItem(DEFAULT_DOCUMENT_NAME)))
            .andExpect(jsonPath("$.[*].documentTitle").value(hasItem(DEFAULT_DOCUMENT_TITLE)))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
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
            .andExpect(jsonPath("$.id").value(organizationDocument.getId()))
            .andExpect(jsonPath("$.documentName").value(DEFAULT_DOCUMENT_NAME))
            .andExpect(jsonPath("$.documentTitle").value(DEFAULT_DOCUMENT_TITLE))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
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
        updatedOrganizationDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .status(UPDATED_STATUS);

        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrganizationDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrganizationDocument))
            )
            .andExpect(status().isOk());

        // Validate the OrganizationDocument in the database
        List<OrganizationDocument> organizationDocumentList = organizationDocumentRepository.findAll();
        assertThat(organizationDocumentList).hasSize(databaseSizeBeforeUpdate);
        OrganizationDocument testOrganizationDocument = organizationDocumentList.get(organizationDocumentList.size() - 1);
        assertThat(testOrganizationDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testOrganizationDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testOrganizationDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testOrganizationDocument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void putNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, organizationDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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

        partialUpdatedOrganizationDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .status(UPDATED_STATUS);

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
        assertThat(testOrganizationDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testOrganizationDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testOrganizationDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testOrganizationDocument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void fullUpdateOrganizationDocumentWithPatch() throws Exception {
        // Initialize the database
        organizationDocumentRepository.save(organizationDocument);

        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();

        // Update the organizationDocument using partial update
        OrganizationDocument partialUpdatedOrganizationDocument = new OrganizationDocument();
        partialUpdatedOrganizationDocument.setId(organizationDocument.getId());

        partialUpdatedOrganizationDocument
            .documentName(UPDATED_DOCUMENT_NAME)
            .documentTitle(UPDATED_DOCUMENT_TITLE)
            .documentType(UPDATED_DOCUMENT_TYPE)
            .status(UPDATED_STATUS);

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
        assertThat(testOrganizationDocument.getDocumentName()).isEqualTo(UPDATED_DOCUMENT_NAME);
        assertThat(testOrganizationDocument.getDocumentTitle()).isEqualTo(UPDATED_DOCUMENT_TITLE);
        assertThat(testOrganizationDocument.getDocumentType()).isEqualTo(UPDATED_DOCUMENT_TYPE);
        assertThat(testOrganizationDocument.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    void patchNonExistingOrganizationDocument() throws Exception {
        int databaseSizeBeforeUpdate = organizationDocumentRepository.findAll().size();
        organizationDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, organizationDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrganizationDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(organizationDocument))
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
