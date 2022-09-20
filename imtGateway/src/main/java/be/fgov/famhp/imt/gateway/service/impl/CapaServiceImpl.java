package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Capa;
import be.fgov.famhp.imt.gateway.repository.CapaRepository;
import be.fgov.famhp.imt.gateway.service.CapaService;
import be.fgov.famhp.imt.gateway.service.dto.CapaDTO;
import be.fgov.famhp.imt.gateway.service.mapper.CapaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Capa}.
 */
@Service
public class CapaServiceImpl implements CapaService {

    private final Logger log = LoggerFactory.getLogger(CapaServiceImpl.class);

    private final CapaRepository capaRepository;

    private final CapaMapper capaMapper;

    public CapaServiceImpl(CapaRepository capaRepository, CapaMapper capaMapper) {
        this.capaRepository = capaRepository;
        this.capaMapper = capaMapper;
    }

    @Override
    public Mono<CapaDTO> save(CapaDTO capaDTO) {
        log.debug("Request to save Capa : {}", capaDTO);
        return capaRepository.save(capaMapper.toEntity(capaDTO)).map(capaMapper::toDto);
    }

    @Override
    public Mono<CapaDTO> update(CapaDTO capaDTO) {
        log.debug("Request to update Capa : {}", capaDTO);
        // no save call needed as we have no fields that can be updated
        return capaRepository.findById(capaDTO.getId()).map(capaMapper::toDto);
    }

    @Override
    public Mono<CapaDTO> partialUpdate(CapaDTO capaDTO) {
        log.debug("Request to partially update Capa : {}", capaDTO);

        return capaRepository
            .findById(capaDTO.getId())
            .map(existingCapa -> {
                capaMapper.partialUpdate(existingCapa, capaDTO);

                return existingCapa;
            })
            // .flatMap(capaRepository::save)
            .map(capaMapper::toDto);
    }

    @Override
    public Flux<CapaDTO> findAll() {
        log.debug("Request to get all Capas");
        return capaRepository.findAll().map(capaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return capaRepository.count();
    }

    @Override
    public Mono<CapaDTO> findOne(String id) {
        log.debug("Request to get Capa : {}", id);
        return capaRepository.findById(id).map(capaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Capa : {}", id);
        return capaRepository.deleteById(id);
    }
}
