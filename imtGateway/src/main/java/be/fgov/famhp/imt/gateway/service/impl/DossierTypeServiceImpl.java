package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.DossierType;
import be.fgov.famhp.imt.gateway.repository.DossierTypeRepository;
import be.fgov.famhp.imt.gateway.service.DossierTypeService;
import be.fgov.famhp.imt.gateway.service.dto.DossierTypeDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierTypeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<DossierTypeDTO> save(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to save DossierType : {}", dossierTypeDTO);
        return dossierTypeRepository.save(dossierTypeMapper.toEntity(dossierTypeDTO)).map(dossierTypeMapper::toDto);
    }

    @Override
    public Mono<DossierTypeDTO> update(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to update DossierType : {}", dossierTypeDTO);
        // no save call needed as we have no fields that can be updated
        return dossierTypeRepository.findById(dossierTypeDTO.getId()).map(dossierTypeMapper::toDto);
    }

    @Override
    public Mono<DossierTypeDTO> partialUpdate(DossierTypeDTO dossierTypeDTO) {
        log.debug("Request to partially update DossierType : {}", dossierTypeDTO);

        return dossierTypeRepository
            .findById(dossierTypeDTO.getId())
            .map(existingDossierType -> {
                dossierTypeMapper.partialUpdate(existingDossierType, dossierTypeDTO);

                return existingDossierType;
            })
            // .flatMap(dossierTypeRepository::save)
            .map(dossierTypeMapper::toDto);
    }

    @Override
    public Flux<DossierTypeDTO> findAll() {
        log.debug("Request to get all DossierTypes");
        return dossierTypeRepository.findAll().map(dossierTypeMapper::toDto);
    }

    public Mono<Long> countAll() {
        return dossierTypeRepository.count();
    }

    @Override
    public Mono<DossierTypeDTO> findOne(String id) {
        log.debug("Request to get DossierType : {}", id);
        return dossierTypeRepository.findById(id).map(dossierTypeMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete DossierType : {}", id);
        return dossierTypeRepository.deleteById(id);
    }
}
