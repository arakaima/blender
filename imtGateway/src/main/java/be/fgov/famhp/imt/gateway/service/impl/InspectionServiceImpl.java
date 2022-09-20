package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Inspection;
import be.fgov.famhp.imt.gateway.repository.InspectionRepository;
import be.fgov.famhp.imt.gateway.service.InspectionService;
import be.fgov.famhp.imt.gateway.service.dto.InspectionDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Inspection}.
 */
@Service
public class InspectionServiceImpl implements InspectionService {

    private final Logger log = LoggerFactory.getLogger(InspectionServiceImpl.class);

    private final InspectionRepository inspectionRepository;

    private final InspectionMapper inspectionMapper;

    public InspectionServiceImpl(InspectionRepository inspectionRepository, InspectionMapper inspectionMapper) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionMapper = inspectionMapper;
    }

    @Override
    public Mono<InspectionDTO> save(InspectionDTO inspectionDTO) {
        log.debug("Request to save Inspection : {}", inspectionDTO);
        return inspectionRepository.save(inspectionMapper.toEntity(inspectionDTO)).map(inspectionMapper::toDto);
    }

    @Override
    public Mono<InspectionDTO> update(InspectionDTO inspectionDTO) {
        log.debug("Request to update Inspection : {}", inspectionDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionRepository.findById(inspectionDTO.getId()).map(inspectionMapper::toDto);
    }

    @Override
    public Mono<InspectionDTO> partialUpdate(InspectionDTO inspectionDTO) {
        log.debug("Request to partially update Inspection : {}", inspectionDTO);

        return inspectionRepository
            .findById(inspectionDTO.getId())
            .map(existingInspection -> {
                inspectionMapper.partialUpdate(existingInspection, inspectionDTO);

                return existingInspection;
            })
            // .flatMap(inspectionRepository::save)
            .map(inspectionMapper::toDto);
    }

    @Override
    public Flux<InspectionDTO> findAll() {
        log.debug("Request to get all Inspections");
        return inspectionRepository.findAll().map(inspectionMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inspectionRepository.count();
    }

    @Override
    public Mono<InspectionDTO> findOne(String id) {
        log.debug("Request to get Inspection : {}", id);
        return inspectionRepository.findById(id).map(inspectionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Inspection : {}", id);
        return inspectionRepository.deleteById(id);
    }
}
