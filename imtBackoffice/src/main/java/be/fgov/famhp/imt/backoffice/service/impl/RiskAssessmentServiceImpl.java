package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import be.fgov.famhp.imt.backoffice.repository.RiskAssessmentRepository;
import be.fgov.famhp.imt.backoffice.service.RiskAssessmentService;
import be.fgov.famhp.imt.backoffice.service.dto.RiskAssessmentDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.RiskAssessmentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public RiskAssessmentDTO save(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to save RiskAssessment : {}", riskAssessmentDTO);
        RiskAssessment riskAssessment = riskAssessmentMapper.toEntity(riskAssessmentDTO);
        riskAssessment = riskAssessmentRepository.save(riskAssessment);
        return riskAssessmentMapper.toDto(riskAssessment);
    }

    @Override
    public RiskAssessmentDTO update(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to update RiskAssessment : {}", riskAssessmentDTO);
        RiskAssessment riskAssessment = riskAssessmentMapper.toEntity(riskAssessmentDTO);
        // no save call needed as we have no fields that can be updated
        return riskAssessmentMapper.toDto(riskAssessment);
    }

    @Override
    public Optional<RiskAssessmentDTO> partialUpdate(RiskAssessmentDTO riskAssessmentDTO) {
        log.debug("Request to partially update RiskAssessment : {}", riskAssessmentDTO);

        return riskAssessmentRepository
            .findById(riskAssessmentDTO.getId())
            .map(existingRiskAssessment -> {
                riskAssessmentMapper.partialUpdate(existingRiskAssessment, riskAssessmentDTO);

                return existingRiskAssessment;
            })
            // .map(riskAssessmentRepository::save)
            .map(riskAssessmentMapper::toDto);
    }

    @Override
    public List<RiskAssessmentDTO> findAll() {
        log.debug("Request to get all RiskAssessments");
        return riskAssessmentRepository
            .findAll()
            .stream()
            .map(riskAssessmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<RiskAssessmentDTO> findOne(String id) {
        log.debug("Request to get RiskAssessment : {}", id);
        return riskAssessmentRepository.findById(id).map(riskAssessmentMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RiskAssessment : {}", id);
        riskAssessmentRepository.deleteById(id);
    }
}
