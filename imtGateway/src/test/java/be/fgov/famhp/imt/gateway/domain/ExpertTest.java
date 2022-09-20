package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpertTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Expert.class);
        Expert expert1 = new Expert();
        expert1.setId("id1");
        Expert expert2 = new Expert();
        expert2.setId(expert1.getId());
        assertThat(expert1).isEqualTo(expert2);
        expert2.setId("id2");
        assertThat(expert1).isNotEqualTo(expert2);
        expert1.setId(null);
        assertThat(expert1).isNotEqualTo(expert2);
    }
}
