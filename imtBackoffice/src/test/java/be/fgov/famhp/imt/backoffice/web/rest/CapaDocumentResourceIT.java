package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import be.fgov.famhp.imt.backoffice.repository.CapaDocumentRepository;
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
 * Integration tests for the {@link CapaDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapaDocumentResourceIT {

    private static final String ENTITY_API_URL = "/api/capa-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CapaDocumentRepository capaDocumentRepository;

    @Autowired
    private MockMvc restCapaDocumentMockMvc;

    private CapaDocument capaDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapaDocument createEntity() {
        CapaDocument capaDocument = new CapaDocument();
        return capaDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CapaDocument createUpdatedEntity() {
        CapaDocument capaDocument = new CapaDocument();
        return capaDocument;
    }

    @BeforeEach
    public void initTest() {
        capaDocumentRepository.deleteAll();
        capaDocument = createEntity();
    }

    @Test
    void createCapaDocument() throws Exception {
        int databaseSizeBeforeCreate = capaDocumentRepository.findAll().size();
        // Create the CapaDocument
        restCapaDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isCreated());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void createCapaDocumentWithExistingId() throws Exception {
        // Create the CapaDocument with an existing ID
        capaDocument.setId("existing_id");

        int databaseSizeBeforeCreate = capaDocumentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapaDocumentMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCapaDocuments() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        // Get all the capaDocumentList
        restCapaDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capaDocument.getId())));
    }

    @Test
    void getCapaDocument() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        // Get the capaDocument
        restCapaDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, capaDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capaDocument.getId()));
    }

    @Test
    void getNonExistingCapaDocument() throws Exception {
        // Get the capaDocument
        restCapaDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCapaDocument() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();

        // Update the capaDocument
        CapaDocument updatedCapaDocument = capaDocumentRepository.findById(capaDocument.getId()).get();

        restCapaDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapaDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCapaDocument))
            )
            .andExpect(status().isOk());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void putNonExistingCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capaDocument.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCapaDocumentWithPatch() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();

        // Update the capaDocument using partial update
        CapaDocument partialUpdatedCapaDocument = new CapaDocument();
        partialUpdatedCapaDocument.setId(capaDocument.getId());

        restCapaDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapaDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapaDocument))
            )
            .andExpect(status().isOk());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void fullUpdateCapaDocumentWithPatch() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();

        // Update the capaDocument using partial update
        CapaDocument partialUpdatedCapaDocument = new CapaDocument();
        partialUpdatedCapaDocument.setId(capaDocument.getId());

        restCapaDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapaDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapaDocument))
            )
            .andExpect(status().isOk());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
        CapaDocument testCapaDocument = capaDocumentList.get(capaDocumentList.size() - 1);
    }

    @Test
    void patchNonExistingCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capaDocument.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isBadRequest());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCapaDocument() throws Exception {
        int databaseSizeBeforeUpdate = capaDocumentRepository.findAll().size();
        capaDocument.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capaDocument))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CapaDocument in the database
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCapaDocument() throws Exception {
        // Initialize the database
        capaDocumentRepository.save(capaDocument);

        int databaseSizeBeforeDelete = capaDocumentRepository.findAll().size();

        // Delete the capaDocument
        restCapaDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, capaDocument.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CapaDocument> capaDocumentList = capaDocumentRepository.findAll();
        assertThat(capaDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
