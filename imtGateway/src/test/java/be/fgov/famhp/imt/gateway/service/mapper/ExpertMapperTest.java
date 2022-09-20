package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExpertMapperTest {

    private ExpertMapper expertMapper;

    @BeforeEach
    public void setUp() {
        expertMapper = new ExpertMapperImpl();
    }
}
