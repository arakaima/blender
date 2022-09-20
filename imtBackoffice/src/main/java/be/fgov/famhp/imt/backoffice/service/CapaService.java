package be.fgov.famhp.imt.backoffice.service;

import be.fgov.famhp.imt.backoffice.service.dto.CapaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link be.fgov.famhp.imt.backoffice.domain.Capa}.
 */
public interface CapaService {
    /**
     * Save a capa.
     *
     * @param capaDTO the entity to save.
     * @return the persisted entity.
     */
    CapaDTO save(CapaDTO capaDTO);

    /**
     * Updates a capa.
     *
     * @param capaDTO the entity to update.
     * @return the persisted entity.
     */
    CapaDTO update(CapaDTO capaDTO);

    /**
     * Partially updates a capa.
     *
     * @param capaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CapaDTO> partialUpdate(CapaDTO capaDTO);

    /**
     * Get all the capas.
     *
     * @return the list of entities.
     */
    List<CapaDTO> findAll();

    /**
     * Get the "id" capa.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CapaDTO> findOne(String id);

    /**
     * Delete the "id" capa.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
