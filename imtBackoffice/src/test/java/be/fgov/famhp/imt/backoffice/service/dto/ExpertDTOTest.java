package be.fgov.famhp.imt.backoffice.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExpertDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExpertDTO.class);
        ExpertDTO expertDTO1 = new ExpertDTO();
        expertDTO1.setId("id1");
        ExpertDTO expertDTO2 = new ExpertDTO();
        assertThat(expertDTO1).isNotEqualTo(expertDTO2);
        expertDTO2.setId(expertDTO1.getId());
        assertThat(expertDTO1).isEqualTo(expertDTO2);
        expertDTO2.setId("id2");
        assertThat(expertDTO1).isNotEqualTo(expertDTO2);
        expertDTO1.setId(null);
        assertThat(expertDTO1).isNotEqualTo(expertDTO2);
    }
}
