package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttachedDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttachedDocument.class);
        AttachedDocument attachedDocument1 = new AttachedDocument();
        attachedDocument1.setId("id1");
        AttachedDocument attachedDocument2 = new AttachedDocument();
        attachedDocument2.setId(attachedDocument1.getId());
        assertThat(attachedDocument1).isEqualTo(attachedDocument2);
        attachedDocument2.setId("id2");
        assertThat(attachedDocument1).isNotEqualTo(attachedDocument2);
        attachedDocument1.setId(null);
        assertThat(attachedDocument1).isNotEqualTo(attachedDocument2);
    }
}
