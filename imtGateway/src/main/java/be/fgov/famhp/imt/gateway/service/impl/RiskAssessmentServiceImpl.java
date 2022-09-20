package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.RiskAssessment;
import be.fgov.famhp.imt.gateway.repository.RiskAssessmentRepository;
import be.fgov.famhp.imt.gateway.service.RiskAssessmentService;
import be.fgov.famhp.imt.gateway.service.dto.RiskAssessmentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.RiskAssessmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RiskAssessment}.
 */
@Service
public class RiskAssessmentServiceImpl implements RiskAssessmentService {

    private final Logger log = LoggerFactory.getLogger(RiskAssessmentServiceImpl.class);

    private final RiskAssessmentRepository riskAssessmentRepository;

    private final RiskAssessmentMapper riskAssessmentMapper;

    public RiskAssessmentServiceImpl(RiskAssessmentRepository riskAssessmentRepository, RiskAssessmentMapper riskAssessmentMapper) {
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.riskAssessmentMapper = riskAssessmentMapper;
    }

    @Override
    public Mono<RiskAssessmentDTO> save(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to save RiskAssessment : {}", riskAssessmentDTO);
        return riskAssessmentRepository.save(riskAssessmentMapper.toEntity(riskAssessmentDTO)).map(riskAssessmentMapper::toDto);
    }

    @Override
    public Mono<RiskAssessmentDTO> update(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to update RiskAssessment : {}", riskAssessmentDTO);
        // no save call needed as we have no fields that can be updated
        return riskAssessmentRepository.findById(riskAssessmentDTO.getId()).map(riskAssessmentMapper::toDto);
    }

    @Override
    public Mono<RiskAssessmentDTO> partialUpdate(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to partially update RiskAssessment : {}", riskAssessmentDTO);

        return riskAssessmentRepository
            .findById(riskAssessmentDTO.getId())
            .map(existingRiskAssessment -> {
                riskAssessmentMapper.partialUpdate(existingRiskAssessment, riskAssessmentDTO);

                return existingRiskAssessment;
            })
            // .flatMap(riskAssessmentRepository::save)
            .map(riskAssessmentMapper::toDto);
    }

    @Override
    public Flux<RiskAssessmentDTO> findAll() {
        log.debug("Request to get all RiskAssessments");
        return riskAssessmentRepository.findAll().map(riskAssessmentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return riskAssessmentRepository.count();
    }

    @Override
    public Mono<RiskAssessmentDTO> findOne(String id) {
        log.debug("Request to get RiskAssessment : {}", id);
        return riskAssessmentRepository.findById(id).map(riskAssessmentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete RiskAssessment : {}", id);
        return riskAssessmentRepository.deleteById(id);
    }
}
