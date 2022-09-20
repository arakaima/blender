package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectionType;
import be.fgov.famhp.imt.backoffice.repository.InspectionTypeRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionTypeService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionTypeDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectionTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public InspectionTypeDTO save(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to save InspectionType : {}", inspectionTypeDTO);
        InspectionType inspectionType = inspectionTypeMapper.toEntity(inspectionTypeDTO);
        inspectionType = inspectionTypeRepository.save(inspectionType);
        return inspectionTypeMapper.toDto(inspectionType);
    }

    @Override
    public InspectionTypeDTO update(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to update InspectionType : {}", inspectionTypeDTO);
        InspectionType inspectionType = inspectionTypeMapper.toEntity(inspectionTypeDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionTypeMapper.toDto(inspectionType);
    }

    @Override
    public Optional<InspectionTypeDTO> partialUpdate(InspectionTypeDTO inspectionTypeDTO) {
        log.debug("Request to partially update InspectionType : {}", inspectionTypeDTO);

        return inspectionTypeRepository
            .findById(inspectionTypeDTO.getId())
            .map(existingInspectionType -> {
                inspectionTypeMapper.partialUpdate(existingInspectionType, inspectionTypeDTO);

                return existingInspectionType;
            })
            // .map(inspectionTypeRepository::save)
            .map(inspectionTypeMapper::toDto);
    }

    @Override
    public List<InspectionTypeDTO> findAll() {
        log.debug("Request to get all InspectionTypes");
        return inspectionTypeRepository
            .findAll()
            .stream()
            .map(inspectionTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<InspectionTypeDTO> findOne(String id) {
        log.debug("Request to get InspectionType : {}", id);
        return inspectionTypeRepository.findById(id).map(inspectionTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectionType : {}", id);
        inspectionTypeRepository.deleteById(id);
    }
}
