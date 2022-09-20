package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ContactPerson}.
 */
public interface ContactPersonService {
    /**
     * Save a contactPerson.
     *
     * @param contactPerson the entity to save.
     * @return the persisted entity.
     */
    ContactPerson save(ContactPerson contactPerson);

    /**
     * Updates a contactPerson.
     *
     * @param contactPerson the entity to update.
     * @return the persisted entity.
     */
    ContactPerson update(ContactPerson contactPerson);

    /**
     * Partially updates a contactPerson.
     *
     * @param contactPerson the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactPerson> partialUpdate(ContactPerson contactPerson);

    /**
     * Get all the contactPeople.
     *
     * @return the list of entities.
     */
    List<ContactPerson> findAll();

    /**
     * Get the "id" contactPerson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactPerson> findOne(String id);

    /**
     * Delete the "id" contactPerson.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
