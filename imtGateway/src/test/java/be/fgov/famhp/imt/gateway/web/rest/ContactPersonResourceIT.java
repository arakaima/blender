package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.ContactPerson;
import be.fgov.famhp.imt.gateway.repository.ContactPersonRepository;
import be.fgov.famhp.imt.gateway.service.dto.ContactPersonDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ContactPersonMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link ContactPersonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ContactPersonResourceIT {

    private static final String ENTITY_API_URL = "/api/contact-people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ContactPersonRepository contactPersonRepository;

    @Autowired
    private ContactPersonMapper contactPersonMapper;

    @Autowired
    private WebTestClient webTestClient;

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
        contactPersonRepository.deleteAll().block();
        contactPerson = createEntity();
    }

    @Test
    void createContactPerson() throws Exception {
        int databaseSizeBeforeCreate = contactPersonRepository.findAll().collectList().block().size();
        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate + 1);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void createContactPersonWithExistingId() throws Exception {
        // Create the ContactPerson with an existing ID
        contactPerson.setId("existing_id");
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        int databaseSizeBeforeCreate = contactPersonRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllContactPeopleAsStream() {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        List<ContactPerson> contactPersonList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ContactPersonDTO.class)
            .getResponseBody()
            .map(contactPersonMapper::toEntity)
            .filter(contactPerson::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(contactPersonList).isNotNull();
        assertThat(contactPersonList).hasSize(1);
        ContactPerson testContactPerson = contactPersonList.get(0);
    }

    @Test
    void getAllContactPeople() {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        // Get all the contactPersonList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(contactPerson.getId()));
    }

    @Test
    void getContactPerson() {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        // Get the contactPerson
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, contactPerson.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(contactPerson.getId()));
    }

    @Test
    void getNonExistingContactPerson() {
        // Get the contactPerson
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingContactPerson() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();

        // Update the contactPerson
        ContactPerson updatedContactPerson = contactPersonRepository.findById(contactPerson.getId()).block();
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(updatedContactPerson);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactPersonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void putNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, contactPersonDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void fullUpdateContactPersonWithPatch() throws Exception {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();

        // Update the contactPerson using partial update
        ContactPerson partialUpdatedContactPerson = new ContactPerson();
        partialUpdatedContactPerson.setId(contactPerson.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedContactPerson.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedContactPerson))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
        ContactPerson testContactPerson = contactPersonList.get(contactPersonList.size() - 1);
    }

    @Test
    void patchNonExistingContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, contactPersonDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamContactPerson() throws Exception {
        int databaseSizeBeforeUpdate = contactPersonRepository.findAll().collectList().block().size();
        contactPerson.setId(UUID.randomUUID().toString());

        // Create the ContactPerson
        ContactPersonDTO contactPersonDTO = contactPersonMapper.toDto(contactPerson);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(contactPersonDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ContactPerson in the database
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteContactPerson() {
        // Initialize the database
        contactPersonRepository.save(contactPerson).block();

        int databaseSizeBeforeDelete = contactPersonRepository.findAll().collectList().block().size();

        // Delete the contactPerson
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, contactPerson.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ContactPerson> contactPersonList = contactPersonRepository.findAll().collectList().block();
        assertThat(contactPersonList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
