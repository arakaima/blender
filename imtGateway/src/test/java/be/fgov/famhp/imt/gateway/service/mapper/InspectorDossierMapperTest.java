package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InspectorDossierMapperTest {

    private InspectorDossierMapper inspectorDossierMapper;

    @BeforeEach
    public void setUp() {
        inspectorDossierMapper = new InspectorDossierMapperImpl();
    }
}
