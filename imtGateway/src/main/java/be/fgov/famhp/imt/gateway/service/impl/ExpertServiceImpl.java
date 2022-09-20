package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Expert;
import be.fgov.famhp.imt.gateway.repository.ExpertRepository;
import be.fgov.famhp.imt.gateway.service.ExpertService;
import be.fgov.famhp.imt.gateway.service.dto.ExpertDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ExpertMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Expert}.
 */
@Service
public class ExpertServiceImpl implements ExpertService {

    private final Logger log = LoggerFactory.getLogger(ExpertServiceImpl.class);

    private final ExpertRepository expertRepository;

    private final ExpertMapper expertMapper;

    public ExpertServiceImpl(ExpertRepository expertRepository, ExpertMapper expertMapper) {
        this.expertRepository = expertRepository;
        this.expertMapper = expertMapper;
    }

    @Override
    public Mono<ExpertDTO> save(ExpertDTO expertDTO) {
        log.debug("Request to save Expert : {}", expertDTO);
        return expertRepository.save(expertMapper.toEntity(expertDTO)).map(expertMapper::toDto);
    }

    @Override
    public Mono<ExpertDTO> update(ExpertDTO expertDTO) {
        log.debug("Request to update Expert : {}", expertDTO);
        // no save call needed as we have no fields that can be updated
        return expertRepository.findById(expertDTO.getId()).map(expertMapper::toDto);
    }

    @Override
    public Mono<ExpertDTO> partialUpdate(ExpertDTO expertDTO) {
        log.debug("Request to partially update Expert : {}", expertDTO);

        return expertRepository
            .findById(expertDTO.getId())
            .map(existingExpert -> {
                expertMapper.partialUpdate(existingExpert, expertDTO);

                return existingExpert;
            })
            // .flatMap(expertRepository::save)
            .map(expertMapper::toDto);
    }

    @Override
    public Flux<ExpertDTO> findAll() {
        log.debug("Request to get all Experts");
        return expertRepository.findAll().map(expertMapper::toDto);
    }

    public Mono<Long> countAll() {
        return expertRepository.count();
    }

    @Override
    public Mono<ExpertDTO> findOne(String id) {
        log.debug("Request to get Expert : {}", id);
        return expertRepository.findById(id).map(expertMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Expert : {}", id);
        return expertRepository.deleteById(id);
    }
}
