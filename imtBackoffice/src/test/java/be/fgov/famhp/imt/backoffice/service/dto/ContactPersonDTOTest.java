package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactPersonDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactPersonDTO.class);
        ContactPersonDTO contactPersonDTO1 = new ContactPersonDTO();
        contactPersonDTO1.setId("id1");
        ContactPersonDTO contactPersonDTO2 = new ContactPersonDTO();
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
        contactPersonDTO2.setId(contactPersonDTO1.getId());
        assertThat(contactPersonDTO1).isEqualTo(contactPersonDTO2);
        contactPersonDTO2.setId("id2");
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
        contactPersonDTO1.setId(null);
        assertThat(contactPersonDTO1).isNotEqualTo(contactPersonDTO2);
    }
}
