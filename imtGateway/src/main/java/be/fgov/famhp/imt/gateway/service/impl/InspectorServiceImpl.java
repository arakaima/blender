package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Inspector;
import be.fgov.famhp.imt.gateway.repository.InspectorRepository;
import be.fgov.famhp.imt.gateway.service.InspectorService;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Inspector}.
 */
@Service
public class InspectorServiceImpl implements InspectorService {

    private final Logger log = LoggerFactory.getLogger(InspectorServiceImpl.class);

    private final InspectorRepository inspectorRepository;

    private final InspectorMapper inspectorMapper;

    public InspectorServiceImpl(InspectorRepository inspectorRepository, InspectorMapper inspectorMapper) {
        this.inspectorRepository = inspectorRepository;
        this.inspectorMapper = inspectorMapper;
    }

    @Override
    public Mono<InspectorDTO> save(InspectorDTO inspectorDTO) {
        log.debug("Request to save Inspector : {}", inspectorDTO);
        return inspectorRepository.save(inspectorMapper.toEntity(inspectorDTO)).map(inspectorMapper::toDto);
    }

    @Override
    public Mono<InspectorDTO> update(InspectorDTO inspectorDTO) {
        log.debug("Request to update Inspector : {}", inspectorDTO);
        // no save call needed as we have no fields that can be updated
        return inspectorRepository.findById(inspectorDTO.getId()).map(inspectorMapper::toDto);
    }

    @Override
    public Mono<InspectorDTO> partialUpdate(InspectorDTO inspectorDTO) {
        log.debug("Request to partially update Inspector : {}", inspectorDTO);

        return inspectorRepository
            .findById(inspectorDTO.getId())
            .map(existingInspector -> {
                inspectorMapper.partialUpdate(existingInspector, inspectorDTO);

                return existingInspector;
            })
            // .flatMap(inspectorRepository::save)
            .map(inspectorMapper::toDto);
    }

    @Override
    public Flux<InspectorDTO> findAll() {
        log.debug("Request to get all Inspectors");
        return inspectorRepository.findAll().map(inspectorMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inspectorRepository.count();
    }

    @Override
    public Mono<InspectorDTO> findOne(String id) {
        log.debug("Request to get Inspector : {}", id);
        return inspectorRepository.findById(id).map(inspectorMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Inspector : {}", id);
        return inspectorRepository.deleteById(id);
    }
}
