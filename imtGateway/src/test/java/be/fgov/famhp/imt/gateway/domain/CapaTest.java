package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CapaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Capa.class);
        Capa capa1 = new Capa();
        capa1.setId("id1");
        Capa capa2 = new Capa();
        capa2.setId(capa1.getId());
        assertThat(capa1).isEqualTo(capa2);
        capa2.setId("id2");
        assertThat(capa1).isNotEqualTo(capa2);
        capa1.setId(null);
        assertThat(capa1).isNotEqualTo(capa2);
    }
}
