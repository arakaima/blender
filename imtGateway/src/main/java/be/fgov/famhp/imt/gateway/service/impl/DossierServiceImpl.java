package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Dossier;
import be.fgov.famhp.imt.gateway.repository.DossierRepository;
import be.fgov.famhp.imt.gateway.service.DossierService;
import be.fgov.famhp.imt.gateway.service.dto.DossierDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DossierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
public class DossierServiceImpl implements DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierServiceImpl.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;

    public DossierServiceImpl(DossierRepository dossierRepository, DossierMapper dossierMapper) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
    }

    @Override
    public Mono<DossierDTO> save(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        return dossierRepository.save(dossierMapper.toEntity(dossierDTO)).map(dossierMapper::toDto);
    }

    @Override
    public Mono<DossierDTO> update(DossierDTO dossierDTO) {
        log.debug("Request to update Dossier : {}", dossierDTO);
        // no save call needed as we have no fields that can be updated
        return dossierRepository.findById(dossierDTO.getId()).map(dossierMapper::toDto);
    }

    @Override
    public Mono<DossierDTO> partialUpdate(DossierDTO dossierDTO) {
        log.debug("Request to partially update Dossier : {}", dossierDTO);

        return dossierRepository
            .findById(dossierDTO.getId())
            .map(existingDossier -> {
                dossierMapper.partialUpdate(existingDossier, dossierDTO);

                return existingDossier;
            })
            // .flatMap(dossierRepository::save)
            .map(dossierMapper::toDto);
    }

    @Override
    public Flux<DossierDTO> findAll() {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll().map(dossierMapper::toDto);
    }

    public Mono<Long> countAll() {
        return dossierRepository.count();
    }

    @Override
    public Mono<DossierDTO> findOne(String id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id).map(dossierMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Dossier : {}", id);
        return dossierRepository.deleteById(id);
    }
}
