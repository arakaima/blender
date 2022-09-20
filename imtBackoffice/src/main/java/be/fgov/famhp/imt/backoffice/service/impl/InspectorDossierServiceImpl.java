package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import be.fgov.famhp.imt.backoffice.repository.InspectorDossierRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorDossierService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDossierDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectorDossierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public InspectorDossierDTO save(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to save InspectorDossier : {}", inspectorDossierDTO);
        InspectorDossier inspectorDossier = inspectorDossierMapper.toEntity(inspectorDossierDTO);
        inspectorDossier = inspectorDossierRepository.save(inspectorDossier);
        return inspectorDossierMapper.toDto(inspectorDossier);
    }

    @Override
    public InspectorDossierDTO update(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to update InspectorDossier : {}", inspectorDossierDTO);
        InspectorDossier inspectorDossier = inspectorDossierMapper.toEntity(inspectorDossierDTO);
        // no save call needed as we have no fields that can be updated
        return inspectorDossierMapper.toDto(inspectorDossier);
    }

    @Override
    public Optional<InspectorDossierDTO> partialUpdate(InspectorDossierDTO inspectorDossierDTO) {
        log.debug("Request to partially update InspectorDossier : {}", inspectorDossierDTO);

        return inspectorDossierRepository
            .findById(inspectorDossierDTO.getId())
            .map(existingInspectorDossier -> {
                inspectorDossierMapper.partialUpdate(existingInspectorDossier, inspectorDossierDTO);

                return existingInspectorDossier;
            })
            // .map(inspectorDossierRepository::save)
            .map(inspectorDossierMapper::toDto);
    }

    @Override
    public List<InspectorDossierDTO> findAll() {
        log.debug("Request to get all InspectorDossiers");
        return inspectorDossierRepository
            .findAll()
            .stream()
            .map(inspectorDossierMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<InspectorDossierDTO> findOne(String id) {
        log.debug("Request to get InspectorDossier : {}", id);
        return inspectorDossierRepository.findById(id).map(inspectorDossierMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectorDossier : {}", id);
        inspectorDossierRepository.deleteById(id);
    }
}
