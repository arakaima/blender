package be.fgov.famhp.imt.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeficiencyMapperTest {

    private DeficiencyMapper deficiencyMapper;

    @BeforeEach
    public void setUp() {
        deficiencyMapper = new DeficiencyMapperImpl();
    }
}
