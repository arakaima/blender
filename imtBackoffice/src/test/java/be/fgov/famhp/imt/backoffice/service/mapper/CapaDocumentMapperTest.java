package be.fgov.famhp.imt.backoffice.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CapaDocumentMapperTest {

    private CapaDocumentMapper capaDocumentMapper;

    @BeforeEach
    public void setUp() {
        capaDocumentMapper = new CapaDocumentMapperImpl();
    }
}
