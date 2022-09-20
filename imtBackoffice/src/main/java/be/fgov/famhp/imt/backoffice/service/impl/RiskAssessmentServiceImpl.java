package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.RiskAssessment;
import be.fgov.famhp.imt.backoffice.repository.RiskAssessmentRepository;
import be.fgov.famhp.imt.backoffice.service.RiskAssessmentService;
import java.util.List;
import java.util.Optional;
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

    public RiskAssessmentServiceImpl(RiskAssessmentRepository riskAssessmentRepository) {
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    @Override
    public RiskAssessment save(RiskAssessment riskAssessment) {
        log.debug("Request to save RiskAssessment : {}", riskAssessment);
        return riskAssessmentRepository.save(riskAssessment);
    }

    @Override
    public RiskAssessment update(RiskAssessment riskAssessment) {
        log.debug("Request to update RiskAssessment : {}", riskAssessment);
        // no save call needed as we have no fields that can be updated
        return riskAssessment;
    }

    @Override
    public Optional<RiskAssessment> partialUpdate(RiskAssessment riskAssessment) {
        log.debug("Request to partially update RiskAssessment : {}", riskAssessment);

        return riskAssessmentRepository
            .findById(riskAssessment.getId())
            .map(existingRiskAssessment -> {
                return existingRiskAssessment;
            })// .map(riskAssessmentRepository::save)
        ;
    }

    @Override
    public List<RiskAssessment> findAll() {
        log.debug("Request to get all RiskAssessments");
        return riskAssessmentRepository.findAll();
    }

    @Override
    public Optional<RiskAssessment> findOne(String id) {
        log.debug("Request to get RiskAssessment : {}", id);
        return riskAssessmentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RiskAssessment : {}", id);
        riskAssessmentRepository.deleteById(id);
    }
}
