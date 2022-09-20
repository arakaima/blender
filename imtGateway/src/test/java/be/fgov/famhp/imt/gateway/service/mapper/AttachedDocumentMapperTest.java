package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttachedDocumentMapperTest {

    private AttachedDocumentMapper attachedDocumentMapper;

    @BeforeEach
    public void setUp() {
        attachedDocumentMapper = new AttachedDocumentMapperImpl();
    }
}
