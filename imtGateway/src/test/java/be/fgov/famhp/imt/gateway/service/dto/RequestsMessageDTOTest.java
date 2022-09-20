package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RequestsMessageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestsMessageDTO.class);
        RequestsMessageDTO requestsMessageDTO1 = new RequestsMessageDTO();
        requestsMessageDTO1.setId("id1");
        RequestsMessageDTO requestsMessageDTO2 = new RequestsMessageDTO();
        assertThat(requestsMessageDTO1).isNotEqualTo(requestsMessageDTO2);
        requestsMessageDTO2.setId(requestsMessageDTO1.getId());
        assertThat(requestsMessageDTO1).isEqualTo(requestsMessageDTO2);
        requestsMessageDTO2.setId("id2");
        assertThat(requestsMessageDTO1).isNotEqualTo(requestsMessageDTO2);
        requestsMessageDTO1.setId(null);
        assertThat(requestsMessageDTO1).isNotEqualTo(requestsMessageDTO2);
    }
}
