package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RiskAssessmentMapperTest {

    private RiskAssessmentMapper riskAssessmentMapper;

    @BeforeEach
    public void setUp() {
        riskAssessmentMapper = new RiskAssessmentMapperImpl();
    }
}
