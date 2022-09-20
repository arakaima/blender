package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Inspection;
import be.fgov.famhp.imt.backoffice.repository.InspectionRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public InspectionDTO save(InspectionDTO inspectionDTO) {
        log.debug("Request to save Inspection : {}", inspectionDTO);
        Inspection inspection = inspectionMapper.toEntity(inspectionDTO);
        inspection = inspectionRepository.save(inspection);
        return inspectionMapper.toDto(inspection);
    }

    @Override
    public InspectionDTO update(InspectionDTO inspectionDTO) {
        log.debug("Request to update Inspection : {}", inspectionDTO);
        Inspection inspection = inspectionMapper.toEntity(inspectionDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionMapper.toDto(inspection);
    }

    @Override
    public Optional<InspectionDTO> partialUpdate(InspectionDTO inspectionDTO) {
        log.debug("Request to partially update Inspection : {}", inspectionDTO);

        return inspectionRepository
            .findById(inspectionDTO.getId())
            .map(existingInspection -> {
                inspectionMapper.partialUpdate(existingInspection, inspectionDTO);

                return existingInspection;
            })
            // .map(inspectionRepository::save)
            .map(inspectionMapper::toDto);
    }

    @Override
    public List<InspectionDTO> findAll() {
        log.debug("Request to get all Inspections");
        return inspectionRepository.findAll().stream().map(inspectionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<InspectionDTO> findOne(String id) {
        log.debug("Request to get Inspection : {}", id);
        return inspectionRepository.findById(id).map(inspectionMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Inspection : {}", id);
        inspectionRepository.deleteById(id);
    }
}
