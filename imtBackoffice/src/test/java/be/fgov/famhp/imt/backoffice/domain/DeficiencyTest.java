package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeficiencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Deficiency.class);
        Deficiency deficiency1 = new Deficiency();
        deficiency1.setId("id1");
        Deficiency deficiency2 = new Deficiency();
        deficiency2.setId(deficiency1.getId());
        assertThat(deficiency1).isEqualTo(deficiency2);
        deficiency2.setId("id2");
        assertThat(deficiency1).isNotEqualTo(deficiency2);
        deficiency1.setId(null);
        assertThat(deficiency1).isNotEqualTo(deficiency2);
    }
}
