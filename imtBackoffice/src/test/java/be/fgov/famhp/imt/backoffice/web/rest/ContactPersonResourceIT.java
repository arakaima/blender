package be.fgov.famhp.imt.backoffice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.fgov.famhp.imt.backoffice.IntegrationTest;
import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import be.fgov.famhp.imt.backoffice.repository.ContactPersonRepository;
import be.fgov.famhp.imt.backoffice.service.dto.ContactPersonDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.ContactPersonMapper;
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
 * Integration tests for the {@link ContactPersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContactPersonResourceIT {

    private static final String ENTITY_API_URL = "/api/contact-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private ContactPersonMapper contactPersonMapper;

    @Autowired
    private MockMvc restContactPersonMockMvc;

    private ContactPerson contactPerson;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPerson createEntity() {
        ContactPerson contactPerson = new ContactPerson();
        return contactPerson;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContactPerson createUpdatedEntity() {
        ContactPerson contactPerson = new ContactPerson();
        return contactPerson;
    }

    @BeforeEach
    public void initTest() {
        contactPersonRepository.deleteAll();
        contactPerson = createEntity();
    }

    @Test
    void createContactPerson() throws Exception {
        int databaseSizeBeforeCreate = contactPersonRepository.findAll().size();
        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);
        restContactPersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate + 1);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void createContactPersonWithExistingId() throws Exception {
        // Create the ContactPerson with an existing ID
        contactPerson.setId("existing_id");
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        int databaseSizeBeforeCreate = contactPersonRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContactPersonMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllContactPeople() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        // Get all the contactPersonList
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contactPerson.getId())));
    }

    @Test
    void getContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        // Get the contactPerson
        restContactPersonMockMvc
            .perform(get(ENTITY_API_URL_ID, contactPerson.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contactPerson.getId()));
    }

    @Test
    void getNonExistingContactPerson() throws Exception {
        // Get the contactPerson
        restContactPersonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson
        ContactPerson updatedContactPerson = contactPersonRepository.findById(contactPerson.getId()).get();
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(updatedContactPerson);

        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void putNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void fullUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            )
            .andExpect(status().isOk());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void patchNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contactPersonDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContactPersonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson);

        int databaseSizeBeforeDelete = contactPersonRepository.findAll().size();

        // Delete the contactPerson
        restContactPersonMockMvc
            .perform(delete(ENTITY_API_URL_ID, contactPerson.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
