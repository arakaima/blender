package be.fgov.famhp.imt.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InspectionTypeMapperTest {

    private InspectionTypeMapper inspectionTypeMapper;

    @BeforeEach
    public void setUp() {
        inspectionTypeMapper = new InspectionTypeMapperImpl();
    }
}
