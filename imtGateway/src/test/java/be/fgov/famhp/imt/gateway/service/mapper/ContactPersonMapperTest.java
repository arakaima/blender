package be.fgov.famhp.imt.gateway.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactPersonMapperTest {

    private ContactPersonMapper contactPersonMapper;

    @BeforeEach
    public void setUp() {
        contactPersonMapper = new ContactPersonMapperImpl();
    }
}
