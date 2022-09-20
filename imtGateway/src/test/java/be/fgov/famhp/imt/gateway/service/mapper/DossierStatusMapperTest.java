package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DossierStatusMapperTest {

    private DossierStatusMapper dossierStatusMapper;

    @BeforeEach
    public void setUp() {
        dossierStatusMapper = new DossierStatusMapperImpl();
    }
}
