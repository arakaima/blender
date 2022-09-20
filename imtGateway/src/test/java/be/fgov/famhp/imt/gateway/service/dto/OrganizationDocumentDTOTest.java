package be.fgov.famhp.imt.gateway.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.gateway.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationDocumentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDocumentDTO.class);
        OrganizationDocumentDTO organizationDocumentDTO1 = new OrganizationDocumentDTO();
        organizationDocumentDTO1.setId("id1");
        OrganizationDocumentDTO organizationDocumentDTO2 = new OrganizationDocumentDTO();
        assertThat(organizationDocumentDTO1).isNotEqualTo(organizationDocumentDTO2);
        organizationDocumentDTO2.setId(organizationDocumentDTO1.getId());
        assertThat(organizationDocumentDTO1).isEqualTo(organizationDocumentDTO2);
        organizationDocumentDTO2.setId("id2");
        assertThat(organizationDocumentDTO1).isNotEqualTo(organizationDocumentDTO2);
        organizationDocumentDTO1.setId(null);
        assertThat(organizationDocumentDTO1).isNotEqualTo(organizationDocumentDTO2);
    }
}
