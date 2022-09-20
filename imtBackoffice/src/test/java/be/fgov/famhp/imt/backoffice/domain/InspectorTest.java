package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InspectorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inspector.class);
        Inspector inspector1 = new Inspector();
        inspector1.setId("id1");
        Inspector inspector2 = new Inspector();
        inspector2.setId(inspector1.getId());
        assertThat(inspector1).isEqualTo(inspector2);
        inspector2.setId("id2");
        assertThat(inspector1).isNotEqualTo(inspector2);
        inspector1.setId(null);
        assertThat(inspector1).isNotEqualTo(inspector2);
    }
}
