package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import be.fgov.famhp.imt.backoffice.repository.AttachedDocumentRepository;
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
 * Integration tests for the {@link AttachedDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AttachedDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/attached-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttachedDocumentRepository attachedDocumentRepository;

    @Autowired
    private MockMvc restAttachedDocumentMockMvc;

    private AttachedDocument attachedDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedDocument createEntity() {
        AttachedDocument attachedDocument = new AttachedDocument();
        return attachedDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AttachedDocument createUpdatedEntity() {
        AttachedDocument attachedDocument = new AttachedDocument();
        return attachedDocument;
    }

    @BeforeEach
    public void initTest() {
        attachedDocumentRepository.deleteAll();
        attachedDocument = createEntity();
    }

    @Test
    void createAttachedDocument() throws Exception {
        int databaseSizeBeforeCreate = attachedDocumentRepository.findAll().size();
        // Create the AttachedDocument
        restAttachedDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isCreated());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void createAttachedDocumentWithExistingId() throws Exception {
        // Create the AttachedDocument with an existing ID
        attachedDocument.setId("existing_id");

        int databaseSizeBeforeCreate = attachedDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttachedDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllAttachedDocuments() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        // Get all the attachedDocumentList
        restAttachedDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attachedDocument.getId())));
    }

    @Test
    void getAttachedDocument() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        // Get the attachedDocument
        restAttachedDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, attachedDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(attachedDocument.getId()));
    }

    @Test
    void getNonExistingAttachedDocument() throws Exception {
        // Get the attachedDocument
        restAttachedDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAttachedDocument() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();

        // Update the attachedDocument
        AttachedDocument updatedAttachedDocument = attachedDocumentRepository.findById(attachedDocument.getId()).get();

        restAttachedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAttachedDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAttachedDocument))
            )
            .andExpect(status().isOk());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void putNonExistingAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, attachedDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttachedDocumentWithPatch() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();

        // Update the attachedDocument using partial update
        AttachedDocument partialUpdatedAttachedDocument = new AttachedDocument();
        partialUpdatedAttachedDocument.setId(attachedDocument.getId());

        restAttachedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachedDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedDocument))
            )
            .andExpect(status().isOk());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void fullUpdateAttachedDocumentWithPatch() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();

        // Update the attachedDocument using partial update
        AttachedDocument partialUpdatedAttachedDocument = new AttachedDocument();
        partialUpdatedAttachedDocument.setId(attachedDocument.getId());

        restAttachedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAttachedDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachedDocument))
            )
            .andExpect(status().isOk());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
        AttachedDocument testAttachedDocument = attachedDocumentList.get(attachedDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, attachedDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttachedDocument() throws Exception {
        int databaseSizeBeforeUpdate = attachedDocumentRepository.findAll().size();
        attachedDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAttachedDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(attachedDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AttachedDocument in the database
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttachedDocument() throws Exception {
        // Initialize the database
        attachedDocumentRepository.save(attachedDocument);

        int databaseSizeBeforeDelete = attachedDocumentRepository.findAll().size();

        // Delete the attachedDocument
        restAttachedDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, attachedDocument.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AttachedDocument> attachedDocumentList = attachedDocumentRepository.findAll();
        assertThat(attachedDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
