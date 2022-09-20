package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapaDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CapaDocument.class);
        CapaDocument capaDocument1 = new CapaDocument();
        capaDocument1.setId("id1");
        CapaDocument capaDocument2 = new CapaDocument();
        capaDocument2.setId(capaDocument1.getId());
        assertThat(capaDocument1).isEqualTo(capaDocument2);
        capaDocument2.setId("id2");
        assertThat(capaDocument1).isNotEqualTo(capaDocument2);
        capaDocument1.setId(null);
        assertThat(capaDocument1).isNotEqualTo(capaDocument2);
    }
}
