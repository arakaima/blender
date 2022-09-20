package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Deficiency;
import be.fgov.famhp.imt.gateway.repository.DeficiencyRepository;
import be.fgov.famhp.imt.gateway.service.DeficiencyService;
import be.fgov.famhp.imt.gateway.service.dto.DeficiencyDTO;
import be.fgov.famhp.imt.gateway.service.mapper.DeficiencyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Deficiency}.
 */
@Service
public class DeficiencyServiceImpl implements DeficiencyService {

    private final Logger log = LoggerFactory.getLogger(DeficiencyServiceImpl.class);

    private final DeficiencyRepository deficiencyRepository;

    private final DeficiencyMapper deficiencyMapper;

    public DeficiencyServiceImpl(DeficiencyRepository deficiencyRepository, DeficiencyMapper deficiencyMapper) {
        this.deficiencyRepository = deficiencyRepository;
        this.deficiencyMapper = deficiencyMapper;
    }

    @Override
    public Mono<DeficiencyDTO> save(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to save Deficiency : {}", deficiencyDTO);
        return deficiencyRepository.save(deficiencyMapper.toEntity(deficiencyDTO)).map(deficiencyMapper::toDto);
    }

    @Override
    public Mono<DeficiencyDTO> update(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to update Deficiency : {}", deficiencyDTO);
        // no save call needed as we have no fields that can be updated
        return deficiencyRepository.findById(deficiencyDTO.getId()).map(deficiencyMapper::toDto);
    }

    @Override
    public Mono<DeficiencyDTO> partialUpdate(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to partially update Deficiency : {}", deficiencyDTO);

        return deficiencyRepository
            .findById(deficiencyDTO.getId())
            .map(existingDeficiency -> {
                deficiencyMapper.partialUpdate(existingDeficiency, deficiencyDTO);

                return existingDeficiency;
            })
            // .flatMap(deficiencyRepository::save)
            .map(deficiencyMapper::toDto);
    }

    @Override
    public Flux<DeficiencyDTO> findAll() {
        log.debug("Request to get all Deficiencies");
        return deficiencyRepository.findAll().map(deficiencyMapper::toDto);
    }

    public Mono<Long> countAll() {
        return deficiencyRepository.count();
    }

    @Override
    public Mono<DeficiencyDTO> findOne(String id) {
        log.debug("Request to get Deficiency : {}", id);
        return deficiencyRepository.findById(id).map(deficiencyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Deficiency : {}", id);
        return deficiencyRepository.deleteById(id);
    }
}
