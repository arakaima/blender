package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Location;
import be.fgov.famhp.imt.gateway.repository.LocationRepository;
import be.fgov.famhp.imt.gateway.service.LocationService;
import be.fgov.famhp.imt.gateway.service.dto.LocationDTO;
import be.fgov.famhp.imt.gateway.service.mapper.LocationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Location}.
 */
@Service
public class LocationServiceImpl implements LocationService {

    private final Logger log = LoggerFactory.getLogger(LocationServiceImpl.class);

    private final LocationRepository locationRepository;

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public Mono<LocationDTO> save(LocationDTO locationDTO) {
        log.debug("Request to save Location : {}", locationDTO);
        return locationRepository.save(locationMapper.toEntity(locationDTO)).map(locationMapper::toDto);
    }

    @Override
    public Mono<LocationDTO> update(LocationDTO locationDTO) {
        log.debug("Request to update Location : {}", locationDTO);
        // no save call needed as we have no fields that can be updated
        return locationRepository.findById(locationDTO.getId()).map(locationMapper::toDto);
    }

    @Override
    public Mono<LocationDTO> partialUpdate(LocationDTO locationDTO) {
        log.debug("Request to partially update Location : {}", locationDTO);

        return locationRepository
            .findById(locationDTO.getId())
            .map(existingLocation -> {
                locationMapper.partialUpdate(existingLocation, locationDTO);

                return existingLocation;
            })
            // .flatMap(locationRepository::save)
            .map(locationMapper::toDto);
    }

    @Override
    public Flux<LocationDTO> findAll() {
        log.debug("Request to get all Locations");
        return locationRepository.findAll().map(locationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return locationRepository.count();
    }

    @Override
    public Mono<LocationDTO> findOne(String id) {
        log.debug("Request to get Location : {}", id);
        return locationRepository.findById(id).map(locationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Location : {}", id);
        return locationRepository.deleteById(id);
    }
}
