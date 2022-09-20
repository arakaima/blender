package be.fgov.famhp.imt.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import be.fgov.famhp.imt.gateway.IntegrationTest;
import be.fgov.famhp.imt.gateway.domain.Activity;
import be.fgov.famhp.imt.gateway.repository.ActivityRepository;
import be.fgov.famhp.imt.gateway.service.dto.ActivityDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ActivityMapper;
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
 * Integration tests for the {@link ActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ActivityResourceIT {

    private static final String ENTITY_API_URL = "/api/activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private WebTestClient webTestClient;

    private Activity activity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createEntity() {
        Activity activity = new Activity();
        return activity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Activity createUpdatedEntity() {
        Activity activity = new Activity();
        return activity;
    }

    @BeforeEach
    public void initTest() {
        activityRepository.deleteAll().block();
        activity = createEntity();
    }

    @Test
    void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().collectList().block().size();
        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activityList.get(activityList.size() - 1);
    }

    @Test
    void createActivityWithExistingId() throws Exception {
        // Create the Activity with an existing ID
        activity.setId("existing_id");
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        int databaseSizeBeforeCreate = activityRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllActivitiesAsStream() {
        // Initialize the database
        activityRepository.save(activity).block();

        List<Activity> activityList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ActivityDTO.class)
            .getResponseBody()
            .map(activityMapper::toEntity)
            .filter(activity::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(activityList).isNotNull();
        assertThat(activityList).hasSize(1);
        Activity testActivity = activityList.get(0);
    }

    @Test
    void getAllActivities() {
        // Initialize the database
        activityRepository.save(activity).block();

        // Get all the activityList
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
            .value(hasItem(activity.getId()));
    }

    @Test
    void getActivity() {
        // Initialize the database
        activityRepository.save(activity).block();

        // Get the activity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, activity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(activity.getId()));
    }

    @Test
    void getNonExistingActivity() {
        // Get the activity
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingActivity() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findById(activity.getId()).block();
        ActivityDTO activityDTO = activityMapper.toDto(updatedActivity);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
    }

    @Test
    void putNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
    }

    @Test
    void fullUpdateActivityWithPatch() throws Exception {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();

        // Update the activity using partial update
        Activity partialUpdatedActivity = new Activity();
        partialUpdatedActivity.setId(activity.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedActivity.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedActivity))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activityList.get(activityList.size() - 1);
    }

    @Test
    void patchNonExistingActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, activityDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamActivity() throws Exception {
        int databaseSizeBeforeUpdate = activityRepository.findAll().collectList().block().size();
        activity.setId(UUID.randomUUID().toString());

        // Create the Activity
        ActivityDTO activityDTO = activityMapper.toDto(activity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(activityDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Activity in the database
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteActivity() {
        // Initialize the database
        activityRepository.save(activity).block();

        int databaseSizeBeforeDelete = activityRepository.findAll().collectList().block().size();

        // Delete the activity
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, activity.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Activity> activityList = activityRepository.findAll().collectList().block();
        assertThat(activityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
