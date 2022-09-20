package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.DossierType;
import be.fgov.famhp.imt.backoffice.repository.DossierTypeRepository;
import be.fgov.famhp.imt.backoffice.service.DossierTypeService;
import be.fgov.famhp.imt.backoffice.service.dto.DossierTypeDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DossierTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DossierType}.
 */
@Service
public class DossierTypeServiceImpl implements DossierTypeService {

    private final Logger log = LoggerFactory.getLogger(DossierTypeServiceImpl.class);

    private final DossierTypeRepository dossierTypeRepository;

    private final DossierTypeMapper dossierTypeMapper;

    public DossierTypeServiceImpl(DossierTypeRepository dossierTypeRepository, DossierTypeMapper dossierTypeMapper) {
        this.dossierTypeRepository = dossierTypeRepository;
        this.dossierTypeMapper = dossierTypeMapper;
    }

    @Override
    public DossierTypeDTO save(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to save DossierType : {}", dossierTypeDTO);
        DossierType dossierType = dossierTypeMapper.toEntity(dossierTypeDTO);
        dossierType = dossierTypeRepository.save(dossierType);
        return dossierTypeMapper.toDto(dossierType);
    }

    @Override
    public DossierTypeDTO update(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to update DossierType : {}", dossierTypeDTO);
        DossierType dossierType = dossierTypeMapper.toEntity(dossierTypeDTO);
        // no save call needed as we have no fields that can be updated
        return dossierTypeMapper.toDto(dossierType);
    }

    @Override
    public Optional<DossierTypeDTO> partialUpdate(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to partially update DossierType : {}", dossierTypeDTO);

        return dossierTypeRepository
            .findById(dossierTypeDTO.getId())
            .map(existingDossierType -> {
                dossierTypeMapper.partialUpdate(existingDossierType, dossierTypeDTO);

                return existingDossierType;
            })
            // .map(dossierTypeRepository::save)
            .map(dossierTypeMapper::toDto);
    }

    @Override
    public List<DossierTypeDTO> findAll() {
        log.debug("Request to get all DossierTypes");
        return dossierTypeRepository.findAll().stream().map(dossierTypeMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<DossierTypeDTO> findOne(String id) {
        log.debug("Request to get DossierType : {}", id);
        return dossierTypeRepository.findById(id).map(dossierTypeMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DossierType : {}", id);
        dossierTypeRepository.deleteById(id);
    }
}
