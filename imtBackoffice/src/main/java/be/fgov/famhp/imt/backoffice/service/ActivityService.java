package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.ActivityDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Activity}.
 */
public interface ActivityService {
    /**
     * Save a activity.
     *
     * @param activityDTO the entity to save.
     * @return the persisted entity.
     */
    ActivityDTO save(ActivityDTO activityDTO);

    /**
     * Updates a activity.
     *
     * @param activityDTO the entity to update.
     * @return the persisted entity.
     */
    ActivityDTO update(ActivityDTO activityDTO);

    /**
     * Partially updates a activity.
     *
     * @param activityDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ActivityDTO> partialUpdate(ActivityDTO activityDTO);

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    List<ActivityDTO> findAll();

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ActivityDTO> findOne(String id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
