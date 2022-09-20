package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.RequestsDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Requests}.
 */
public interface RequestsService {
    /**
     * Save a requests.
     *
     * @param requestsDTO the entity to save.
     * @return the persisted entity.
     */
    RequestsDTO save(RequestsDTO requestsDTO);

    /**
     * Updates a requests.
     *
     * @param requestsDTO the entity to update.
     * @return the persisted entity.
     */
    RequestsDTO update(RequestsDTO requestsDTO);

    /**
     * Partially updates a requests.
     *
     * @param requestsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<RequestsDTO> partialUpdate(RequestsDTO requestsDTO);

    /**
     * Get all the requests.
     *
     * @return the list of entities.
     */
    List<RequestsDTO> findAll();

    /**
     * Get the "id" requests.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RequestsDTO> findOne(String id);

    /**
     * Delete the "id" requests.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
