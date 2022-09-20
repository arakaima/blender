package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.ContactPersonDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.ContactPerson}.
 */
public interface ContactPersonService {
    /**
     * Save a contactPerson.
     *
     * @param contactPersonDTO the entity to save.
     * @return the persisted entity.
     */
    ContactPersonDTO save(ContactPersonDTO contactPersonDTO);

    /**
     * Updates a contactPerson.
     *
     * @param contactPersonDTO the entity to update.
     * @return the persisted entity.
     */
    ContactPersonDTO update(ContactPersonDTO contactPersonDTO);

    /**
     * Partially updates a contactPerson.
     *
     * @param contactPersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO);

    /**
     * Get all the contactPeople.
     *
     * @return the list of entities.
     */
    List<ContactPersonDTO> findAll();

    /**
     * Get the "id" contactPerson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ContactPersonDTO> findOne(String id);

    /**
     * Delete the "id" contactPerson.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
