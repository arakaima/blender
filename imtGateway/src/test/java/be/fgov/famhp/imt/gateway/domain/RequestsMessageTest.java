package be.fgov.famhp.imt.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestsMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestsMessage.class);
        RequestsMessage requestsMessage1 = new RequestsMessage();
        requestsMessage1.setId("id1");
        RequestsMessage requestsMessage2 = new RequestsMessage();
        requestsMessage2.setId(requestsMessage1.getId());
        assertThat(requestsMessage1).isEqualTo(requestsMessage2);
        requestsMessage2.setId("id2");
        assertThat(requestsMessage1).isNotEqualTo(requestsMessage2);
        requestsMessage1.setId(null);
        assertThat(requestsMessage1).isNotEqualTo(requestsMessage2);
    }
}
