package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requests.class);
        Requests requests1 = new Requests();
        requests1.setId("id1");
        Requests requests2 = new Requests();
        requests2.setId(requests1.getId());
        assertThat(requests1).isEqualTo(requests2);
        requests2.setId("id2");
        assertThat(requests1).isNotEqualTo(requests2);
        requests1.setId(null);
        assertThat(requests1).isNotEqualTo(requests2);
    }
}
