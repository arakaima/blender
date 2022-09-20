package be.fgov.famhp.imt.backoffice.web.rest;

import be.fgov.famhp.imt.backoffice.repository.LocationRepository;
import be.fgov.famhp.imt.backoffice.service.LocationService;
import be.fgov.famhp.imt.backoffice.service.dto.LocationDTO;
import be.fgov.famhp.imt.backoffice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link be.fgov.famhp.imt.backoffice.domain.Location}.
 */
@RestController
@RequestMapping("/api")
public class LocationResource {

    private final Logger log = LoggerFactory.getLogger(LocationResource.class);

    private static final String ENTITY_NAME = "imtBackofficeLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationService locationService;

    private final LocationRepository locationRepository;

    public LocationResource(LocationService locationService, LocationRepository locationRepository) {
        this.locationService = locationService;
        this.locationRepository = locationRepository;
    }

    /**
     * {@code POST  /locations} : Create a new location.
     *
     * @param locationDTO the locationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationDTO, or with status {@code 400 (Bad Request)} if the location has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/locations")
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) throws URISyntaxException {
        log.debug("REST request to save Location : {}", locationDTO);
        if (locationDTO.getId() != null) {
            throw new BadRequestAlertException("A new location cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationDTO result = locationService.save(locationDTO);
        return ResponseEntity
            .created(new URI("/api/locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
            .body(result);
    }

    /**
     * {@code PUT  /locations/:id} : Updates an existing location.
     *
     * @param id the id of the locationDTO to save.
     * @param locationDTO the locationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationDTO,
     * or with status {@code 400 (Bad Request)} if the locationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/locations/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody LocationDTO locationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Location : {}, {}", id, locationDTO);
        if (locationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationDTO result = locationService.update(locationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationDTO.getId()))
            .body(result);
    }

    /**
     * {@code PATCH  /locations/:id} : Partial updates given fields of an existing location, field will ignore if it is null
     *
     * @param id the id of the locationDTO to save.
     * @param locationDTO the locationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationDTO,
     * or with status {@code 400 (Bad Request)} if the locationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the locationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/locations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocationDTO> partialUpdateLocation(
        @PathVariable(value = "id", required = false) final String id,
        @RequestBody LocationDTO locationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Location partially : {}, {}", id, locationDTO);
        if (locationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationDTO> result = locationService.partialUpdate(locationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationDTO.getId())
        );
    }

    /**
     * {@code GET  /locations} : get all the locations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locations in body.
     */
    @GetMapping("/locations")
    public List<LocationDTO> getAllLocations() {
        log.debug("REST request to get all Locations");
        return locationService.findAll();
    }

    /**
     * {@code GET  /locations/:id} : get the "id" location.
     *
     * @param id the id of the locationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable String id) {
        log.debug("REST request to get Location : {}", id);
        Optional<LocationDTO> locationDTO = locationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(locationDTO);
    }

    /**
     * {@code DELETE  /locations/:id} : delete the "id" location.
     *
     * @param id the id of the locationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/locations/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        log.debug("REST request to delete Location : {}", id);
        locationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
