package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Site;
import be.fgov.famhp.imt.gateway.repository.SiteRepository;
import be.fgov.famhp.imt.gateway.service.dto.SiteDTO;
import be.fgov.famhp.imt.gateway.service.mapper.SiteMapper;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SiteResourceIT {

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Site site;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity() {
        Site site = new Site();
        return site;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity() {
        Site site = new Site();
        return site;
    }

    @BeforeEach
    public void initTest() {
        siteRepository.deleteAll().block();
        site = createEntity();
    }

    @Test
    void createSite() throws Exception {
        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate + 1);
        Site testSite = siteList.get(siteList.size() - 1);
    }

    @Test
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId("existing_id");
        SiteDTO siteDTO = siteMapper.toDto(site);

        int databaseSizeBeforeCreate = siteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSitesAsStream() {
        // Initialize the database
        siteRepository.save(site).block();

        List<Site> siteList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SiteDTO.class)
            .getResponseBody()
            .map(siteMapper::toEntity)
            .filter(site::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(siteList).isNotNull();
        assertThat(siteList).hasSize(1);
        Site testSite = siteList.get(0);
    }

    @Test
    void getAllSites() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get all the siteList
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
            .value(hasItem(site.getId()));
    }

    @Test
    void getSite() {
        // Initialize the database
        siteRepository.save(site).block();

        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(site.getId()));
    }

    @Test
    void getNonExistingSite() {
        // Get the site
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSite() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).block();
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
    }

    @Test
    void putNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
    }

    @Test
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSite.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSite))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
        Site testSite = siteList.get(siteList.size() - 1);
    }

    @Test
    void patchNonExistingSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, siteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSite() throws Exception {
        int databaseSizeBeforeUpdate = siteRepository.findAll().collectList().block().size();
        site.setId(UUID.randomUUID().toString());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(siteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Site in the database
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSite() {
        // Initialize the database
        siteRepository.save(site).block();

        int databaseSizeBeforeDelete = siteRepository.findAll().collectList().block().size();

        // Delete the site
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, site.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Site> siteList = siteRepository.findAll().collectList().block();
        assertThat(siteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
