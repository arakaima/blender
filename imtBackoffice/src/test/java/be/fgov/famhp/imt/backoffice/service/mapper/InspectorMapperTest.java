package be.fgov.famhp.imt.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InspectorMapperTest {

    private InspectorMapper inspectorMapper;

    @BeforeEach
    public void setUp() {
        inspectorMapper = new InspectorMapperImpl();
    }
}
