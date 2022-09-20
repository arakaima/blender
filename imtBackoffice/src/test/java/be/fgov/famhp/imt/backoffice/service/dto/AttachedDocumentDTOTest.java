package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachedDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachedDocumentDTO.class);
        AttachedDocumentDTO attachedDocumentDTO1 = new AttachedDocumentDTO();
        attachedDocumentDTO1.setId("id1");
        AttachedDocumentDTO attachedDocumentDTO2 = new AttachedDocumentDTO();
        assertThat(attachedDocumentDTO1).isNotEqualTo(attachedDocumentDTO2);
        attachedDocumentDTO2.setId(attachedDocumentDTO1.getId());
        assertThat(attachedDocumentDTO1).isEqualTo(attachedDocumentDTO2);
        attachedDocumentDTO2.setId("id2");
        assertThat(attachedDocumentDTO1).isNotEqualTo(attachedDocumentDTO2);
        attachedDocumentDTO1.setId(null);
        assertThat(attachedDocumentDTO1).isNotEqualTo(attachedDocumentDTO2);
    }
}
