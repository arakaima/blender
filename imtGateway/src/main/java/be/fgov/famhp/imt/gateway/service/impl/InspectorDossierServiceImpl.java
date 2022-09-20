package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.InspectorDossier;
import be.fgov.famhp.imt.gateway.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.gateway.service.InspectorDossierService;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDossierDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectorDossierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link InspectorDossier}.
 */
@Service
public class InspectorDossierServiceImpl implements InspectorDossierService {

    private final Logger log = LoggerFactory.getLogger(InspectorDossierServiceImpl.class);

    private final InspectorDossierRepository inspectorDossierRepository;

    private final InspectorDossierMapper inspectorDossierMapper;

    public InspectorDossierServiceImpl(
        InspectorDossierRepository inspectorDossierRepository,
        InspectorDossierMapper inspectorDossierMapper
    ) {
        this.inspectorDossierRepository = inspectorDossierRepository;
        this.inspectorDossierMapper = inspectorDossierMapper;
    }

    @Override
    public Mono<InspectorDossierDTO> save(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to save InspectorDossier : {}", inspectorDossierDTO);
        return inspectorDossierRepository.save(inspectorDossierMapper.toEntity(inspectorDossierDTO)).map(inspectorDossierMapper::toDto);
    }

    @Override
    public Mono<InspectorDossierDTO> update(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to update InspectorDossier : {}", inspectorDossierDTO);
        // no save call needed as we have no fields that can be updated
        return inspectorDossierRepository.findById(inspectorDossierDTO.getId()).map(inspectorDossierMapper::toDto);
    }

    @Override
    public Mono<InspectorDossierDTO> partialUpdate(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to partially update InspectorDossier : {}", inspectorDossierDTO);

        return inspectorDossierRepository
            .findById(inspectorDossierDTO.getId())
            .map(existingInspectorDossier -> {
                inspectorDossierMapper.partialUpdate(existingInspectorDossier, inspectorDossierDTO);

                return existingInspectorDossier;
            })
            // .flatMap(inspectorDossierRepository::save)
            .map(inspectorDossierMapper::toDto);
    }

    @Override
    public Flux<InspectorDossierDTO> findAll() {
        log.debug("Request to get all InspectorDossiers");
        return inspectorDossierRepository.findAll().map(inspectorDossierMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inspectorDossierRepository.count();
    }

    @Override
    public Mono<InspectorDossierDTO> findOne(String id) {
        log.debug("Request to get InspectorDossier : {}", id);
        return inspectorDossierRepository.findById(id).map(inspectorDossierMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete InspectorDossier : {}", id);
        return inspectorDossierRepository.deleteById(id);
    }
}
