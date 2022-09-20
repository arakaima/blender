package be.fgov.famhp.imt.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CapaMapperTest {

    private CapaMapper capaMapper;

    @BeforeEach
    public void setUp() {
        capaMapper = new CapaMapperImpl();
    }
}
