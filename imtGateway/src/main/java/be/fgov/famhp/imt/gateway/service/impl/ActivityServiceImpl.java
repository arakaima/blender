package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Activity;
import be.fgov.famhp.imt.gateway.repository.ActivityRepository;
import be.fgov.famhp.imt.gateway.service.ActivityService;
import be.fgov.famhp.imt.gateway.service.dto.ActivityDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ActivityMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Activity}.
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    private final Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);

    private final ActivityRepository activityRepository;

    private final ActivityMapper activityMapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Override
    public Mono<ActivityDTO> save(ActivityDTO activityDTO) {
        log.debug("Request to save Activity : {}", activityDTO);
        return activityRepository.save(activityMapper.toEntity(activityDTO)).map(activityMapper::toDto);
    }

    @Override
    public Mono<ActivityDTO> update(ActivityDTO activityDTO) {
        log.debug("Request to update Activity : {}", activityDTO);
        // no save call needed as we have no fields that can be updated
        return activityRepository.findById(activityDTO.getId()).map(activityMapper::toDto);
    }

    @Override
    public Mono<ActivityDTO> partialUpdate(ActivityDTO activityDTO) {
        log.debug("Request to partially update Activity : {}", activityDTO);

        return activityRepository
            .findById(activityDTO.getId())
            .map(existingActivity -> {
                activityMapper.partialUpdate(existingActivity, activityDTO);

                return existingActivity;
            })
            // .flatMap(activityRepository::save)
            .map(activityMapper::toDto);
    }

    @Override
    public Flux<ActivityDTO> findAll() {
        log.debug("Request to get all Activities");
        return activityRepository.findAll().map(activityMapper::toDto);
    }

    public Mono<Long> countAll() {
        return activityRepository.count();
    }

    @Override
    public Mono<ActivityDTO> findOne(String id) {
        log.debug("Request to get Activity : {}", id);
        return activityRepository.findById(id).map(activityMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Activity : {}", id);
        return activityRepository.deleteById(id);
    }
}
