package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Organization;
import be.fgov.famhp.imt.gateway.repository.OrganizationRepository;
import be.fgov.famhp.imt.gateway.service.dto.OrganizationDTO;
import be.fgov.famhp.imt.gateway.service.mapper.OrganizationMapper;
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
 * Integration tests for the {@link OrganizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class OrganizationResourceIT {

    private static final String ENTITY_API_URL = "/api/organizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Organization organization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createEntity() {
        Organization organization = new Organization();
        return organization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Organization createUpdatedEntity() {
        Organization organization = new Organization();
        return organization;
    }

    @BeforeEach
    public void initTest() {
        organizationRepository.deleteAll().block();
        organization = createEntity();
    }

    @Test
    void createOrganization() throws Exception {
        int databaseSizeBeforeCreate = organizationRepository.findAll().collectList().block().size();
        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate + 1);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
    }

    @Test
    void createOrganizationWithExistingId() throws Exception {
        // Create the Organization with an existing ID
        organization.setId("existing_id");
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        int databaseSizeBeforeCreate = organizationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllOrganizationsAsStream() {
        // Initialize the database
        organizationRepository.save(organization).block();

        List<Organization> organizationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(OrganizationDTO.class)
            .getResponseBody()
            .map(organizationMapper::toEntity)
            .filter(organization::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(organizationList).isNotNull();
        assertThat(organizationList).hasSize(1);
        Organization testOrganization = organizationList.get(0);
    }

    @Test
    void getAllOrganizations() {
        // Initialize the database
        organizationRepository.save(organization).block();

        // Get all the organizationList
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
            .value(hasItem(organization.getId()));
    }

    @Test
    void getOrganization() {
        // Initialize the database
        organizationRepository.save(organization).block();

        // Get the organization
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, organization.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(organization.getId()));
    }

    @Test
    void getNonExistingOrganization() {
        // Get the organization
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingOrganization() throws Exception {
        // Initialize the database
        organizationRepository.save(organization).block();

        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();

        // Update the organization
        Organization updatedOrganization = organizationRepository.findById(organization.getId()).block();
        OrganizationDTO organizationDTO = organizationMapper.toDto(updatedOrganization);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, organizationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
    }

    @Test
    void putNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, organizationDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.save(organization).block();

        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
    }

    @Test
    void fullUpdateOrganizationWithPatch() throws Exception {
        // Initialize the database
        organizationRepository.save(organization).block();

        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();

        // Update the organization using partial update
        Organization partialUpdatedOrganization = new Organization();
        partialUpdatedOrganization.setId(organization.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrganization.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrganization))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
        Organization testOrganization = organizationList.get(organizationList.size() - 1);
    }

    @Test
    void patchNonExistingOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, organizationDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrganization() throws Exception {
        int databaseSizeBeforeUpdate = organizationRepository.findAll().collectList().block().size();
        organization.setId(UUID.randomUUID().toString());

        // Create the Organization
        OrganizationDTO organizationDTO = organizationMapper.toDto(organization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(organizationDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Organization in the database
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrganization() {
        // Initialize the database
        organizationRepository.save(organization).block();

        int databaseSizeBeforeDelete = organizationRepository.findAll().collectList().block().size();

        // Delete the organization
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, organization.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Organization> organizationList = organizationRepository.findAll().collectList().block();
        assertThat(organizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
