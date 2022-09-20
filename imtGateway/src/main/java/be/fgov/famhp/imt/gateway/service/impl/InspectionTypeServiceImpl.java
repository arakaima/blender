package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.InspectionType;
import be.fgov.famhp.imt.gateway.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.gateway.service.InspectionTypeService;
import be.fgov.famhp.imt.gateway.service.dto.InspectionTypeDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link InspectionType}.
 */
@Service
public class InspectionTypeServiceImpl implements InspectionTypeService {

    private final Logger log = LoggerFactory.getLogger(InspectionTypeServiceImpl.class);

    private final InspectionTypeRepository inspectionTypeRepository;

    private final InspectionTypeMapper inspectionTypeMapper;

    public InspectionTypeServiceImpl(InspectionTypeRepository inspectionTypeRepository, InspectionTypeMapper inspectionTypeMapper) {
        this.inspectionTypeRepository = inspectionTypeRepository;
        this.inspectionTypeMapper = inspectionTypeMapper;
    }

    @Override
    public Mono<InspectionTypeDTO> save(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to save InspectionType : {}", inspectionTypeDTO);
        return inspectionTypeRepository.save(inspectionTypeMapper.toEntity(inspectionTypeDTO)).map(inspectionTypeMapper::toDto);
    }

    @Override
    public Mono<InspectionTypeDTO> update(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to update InspectionType : {}", inspectionTypeDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionTypeRepository.findById(inspectionTypeDTO.getId()).map(inspectionTypeMapper::toDto);
    }

    @Override
    public Mono<InspectionTypeDTO> partialUpdate(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to partially update InspectionType : {}", inspectionTypeDTO);

        return inspectionTypeRepository
            .findById(inspectionTypeDTO.getId())
            .map(existingInspectionType -> {
                inspectionTypeMapper.partialUpdate(existingInspectionType, inspectionTypeDTO);

                return existingInspectionType;
            })
            // .flatMap(inspectionTypeRepository::save)
            .map(inspectionTypeMapper::toDto);
    }

    @Override
    public Flux<InspectionTypeDTO> findAll() {
        log.debug("Request to get all InspectionTypes");
        return inspectionTypeRepository.findAll().map(inspectionTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inspectionTypeRepository.count();
    }

    @Override
    public Mono<InspectionTypeDTO> findOne(String id) {
        log.debug("Request to get InspectionType : {}", id);
        return inspectionTypeRepository.findById(id).map(inspectionTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete InspectionType : {}", id);
        return inspectionTypeRepository.deleteById(id);
    }
}
