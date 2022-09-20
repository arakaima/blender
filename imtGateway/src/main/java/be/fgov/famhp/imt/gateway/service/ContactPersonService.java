package be.fgov.famhp.imt.gateway.service;

import be.fgov.famhp.imt.gateway.service.dto.ContactPersonDTO;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.gateway.domain.ContactPerson}.
 */
public interface ContactPersonService {
    /**
     * Save a contactPerson.
     *
     * @param contactPersonDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<ContactPersonDTO> save(ContactPersonDTO contactPersonDTO);

    /**
     * Updates a contactPerson.
     *
     * @param contactPersonDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<ContactPersonDTO> update(ContactPersonDTO contactPersonDTO);

    /**
     * Partially updates a contactPerson.
     *
     * @param contactPersonDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO);

    /**
     * Get all the contactPeople.
     *
     * @return the list of entities.
     */
    Flux<ContactPersonDTO> findAll();

    /**
     * Returns the number of contactPeople available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" contactPerson.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ContactPersonDTO> findOne(String id);

    /**
     * Delete the "id" contactPerson.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
