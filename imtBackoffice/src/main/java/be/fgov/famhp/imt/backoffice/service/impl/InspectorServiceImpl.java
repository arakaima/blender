package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Inspector;
import be.fgov.famhp.imt.backoffice.repository.InspectorRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public InspectorDTO save(InspectorDTO inspectorDTO) {
        log.debug("Request to save Inspector : {}", inspectorDTO);
        Inspector inspector = inspectorMapper.toEntity(inspectorDTO);
        inspector = inspectorRepository.save(inspector);
        return inspectorMapper.toDto(inspector);
    }

    @Override
    public InspectorDTO update(InspectorDTO inspectorDTO) {
        log.debug("Request to update Inspector : {}", inspectorDTO);
        Inspector inspector = inspectorMapper.toEntity(inspectorDTO);
        // no save call needed as we have no fields that can be updated
        return inspectorMapper.toDto(inspector);
    }

    @Override
    public Optional<InspectorDTO> partialUpdate(InspectorDTO inspectorDTO) {
        log.debug("Request to partially update Inspector : {}", inspectorDTO);

        return inspectorRepository
            .findById(inspectorDTO.getId())
            .map(existingInspector -> {
                inspectorMapper.partialUpdate(existingInspector, inspectorDTO);

                return existingInspector;
            })
            // .map(inspectorRepository::save)
            .map(inspectorMapper::toDto);
    }

    @Override
    public List<InspectorDTO> findAll() {
        log.debug("Request to get all Inspectors");
        return inspectorRepository.findAll().stream().map(inspectorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<InspectorDTO> findOne(String id) {
        log.debug("Request to get Inspector : {}", id);
        return inspectorRepository.findById(id).map(inspectorMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Inspector : {}", id);
        inspectorRepository.deleteById(id);
    }
}
