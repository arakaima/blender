package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.Activity;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Activity}.
 */
public interface ActivityService {
    /**
     * Save a activity.
     *
     * @param activity the entity to save.
     * @return the persisted entity.
     */
    Activity save(Activity activity);

    /**
     * Updates a activity.
     *
     * @param activity the entity to update.
     * @return the persisted entity.
     */
    Activity update(Activity activity);

    /**
     * Partially updates a activity.
     *
     * @param activity the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Activity> partialUpdate(Activity activity);

    /**
     * Get all the activities.
     *
     * @return the list of entities.
     */
    List<Activity> findAll();

    /**
     * Get the "id" activity.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Activity> findOne(String id);

    /**
     * Delete the "id" activity.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
