package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.Capa;
import be.fgov.famhp.imt.backoffice.repository.CapaRepository;
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
 * Integration tests for the {@link CapaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CapaResourceIT {

    private static final String ENTITY_API_URL = "/api/capas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private CapaRepository capaRepository;

    @Autowired
    private MockMvc restCapaMockMvc;

    private Capa capa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capa createEntity() {
        Capa capa = new Capa();
        return capa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Capa createUpdatedEntity() {
        Capa capa = new Capa();
        return capa;
    }

    @BeforeEach
    public void initTest() {
        capaRepository.deleteAll();
        capa = createEntity();
    }

    @Test
    void createCapa() throws Exception {
        int databaseSizeBeforeCreate = capaRepository.findAll().size();
        // Create the Capa
        restCapaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isCreated());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeCreate + 1);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void createCapaWithExistingId() throws Exception {
        // Create the Capa with an existing ID
        capa.setId("existing_id");

        int databaseSizeBeforeCreate = capaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCapaMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllCapas() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        // Get all the capaList
        restCapaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(capa.getId())));
    }

    @Test
    void getCapa() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        // Get the capa
        restCapaMockMvc
            .perform(get(ENTITY_API_URL_ID, capa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(capa.getId()));
    }

    @Test
    void getNonExistingCapa() throws Exception {
        // Get the capa
        restCapaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCapa() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        int databaseSizeBeforeUpdate = capaRepository.findAll().size();

        // Update the capa
        Capa updatedCapa = capaRepository.findById(capa.getId()).get();

        restCapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCapa.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCapa))
            )
            .andExpect(status().isOk());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void putNonExistingCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, capa.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCapaWithPatch() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        int databaseSizeBeforeUpdate = capaRepository.findAll().size();

        // Update the capa using partial update
        Capa partialUpdatedCapa = new Capa();
        partialUpdatedCapa.setId(capa.getId());

        restCapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapa.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapa))
            )
            .andExpect(status().isOk());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void fullUpdateCapaWithPatch() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        int databaseSizeBeforeUpdate = capaRepository.findAll().size();

        // Update the capa using partial update
        Capa partialUpdatedCapa = new Capa();
        partialUpdatedCapa.setId(capa.getId());

        restCapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCapa.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCapa))
            )
            .andExpect(status().isOk());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
        Capa testCapa = capaList.get(capaList.size() - 1);
    }

    @Test
    void patchNonExistingCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, capa.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isBadRequest());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCapa() throws Exception {
        int databaseSizeBeforeUpdate = capaRepository.findAll().size();
        capa.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCapaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(capa))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Capa in the database
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCapa() throws Exception {
        // Initialize the database
        capaRepository.save(capa);

        int databaseSizeBeforeDelete = capaRepository.findAll().size();

        // Delete the capa
        restCapaMockMvc
            .perform(delete(ENTITY_API_URL_ID, capa.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Capa> capaList = capaRepository.findAll();
        assertThat(capaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
