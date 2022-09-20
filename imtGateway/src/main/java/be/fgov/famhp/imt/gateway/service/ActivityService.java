package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.ActivityDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.Activity}.
 */
public interface ActivityService {
    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> save(ActivityDTO activityDTO);

    /**
     * Updates a activity.
     *
     * @param activityDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> update(ActivityDTO activityDTO);

    /**
     * Partially updates a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ActivityDTO> partialUpdate(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    Flux<ActivityDTO> findAll();

    /**
     * Returns the number of activities available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ActivityDTO> findOne(String id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
