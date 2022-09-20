package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.DossierStatus;
import be.fgov.famhp.imt.gateway.repository.DossierStatusRepository;
import be.fgov.famhp.imt.gateway.service.DossierStatusService;
import be.fgov.famhp.imt.gateway.service.dto.DossierStatusDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link DossierStatus}.
 */
@Service
public class DossierStatusServiceImpl implements DossierStatusService {

    private final Logger log = LoggerFactory.getLogger(DossierStatusServiceImpl.class);

    private final DossierStatusRepository dossierStatusRepository;

    private final DossierStatusMapper dossierStatusMapper;

    public DossierStatusServiceImpl(DossierStatusRepository dossierStatusRepository, DossierStatusMapper dossierStatusMapper) {
        this.dossierStatusRepository = dossierStatusRepository;
        this.dossierStatusMapper = dossierStatusMapper;
    }

    @Override
    public Mono<DossierStatusDTO> save(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to save DossierStatus : {}", dossierStatusDTO);
        return dossierStatusRepository.save(dossierStatusMapper.toEntity(dossierStatusDTO)).map(dossierStatusMapper::toDto);
    }

    @Override
    public Mono<DossierStatusDTO> update(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to update DossierStatus : {}", dossierStatusDTO);
        // no save call needed as we have no fields that can be updated
        return dossierStatusRepository.findById(dossierStatusDTO.getId()).map(dossierStatusMapper::toDto);
    }

    @Override
    public Mono<DossierStatusDTO> partialUpdate(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to partially update DossierStatus : {}", dossierStatusDTO);

        return dossierStatusRepository
            .findById(dossierStatusDTO.getId())
            .map(existingDossierStatus -> {
                dossierStatusMapper.partialUpdate(existingDossierStatus, dossierStatusDTO);

                return existingDossierStatus;
            })
            // .flatMap(dossierStatusRepository::save)
            .map(dossierStatusMapper::toDto);
    }

    @Override
    public Flux<DossierStatusDTO> findAll() {
        log.debug("Request to get all DossierStatuses");
        return dossierStatusRepository.findAll().map(dossierStatusMapper::toDto);
    }

    public Mono<Long> countAll() {
        return dossierStatusRepository.count();
    }

    @Override
    public Mono<DossierStatusDTO> findOne(String id) {
        log.debug("Request to get DossierStatus : {}", id);
        return dossierStatusRepository.findById(id).map(dossierStatusMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete DossierStatus : {}", id);
        return dossierStatusRepository.deleteById(id);
    }
}
