package be.fgov.famhp.imt.backoffice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.fgov.famhp.imt.backoffice.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrganizationDocumentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationDocument.class);
        OrganizationDocument organizationDocument1 = new OrganizationDocument();
        organizationDocument1.setId("id1");
        OrganizationDocument organizationDocument2 = new OrganizationDocument();
        organizationDocument2.setId(organizationDocument1.getId());
        assertThat(organizationDocument1).isEqualTo(organizationDocument2);
        organizationDocument2.setId("id2");
        assertThat(organizationDocument1).isNotEqualTo(organizationDocument2);
        organizationDocument1.setId(null);
        assertThat(organizationDocument1).isNotEqualTo(organizationDocument2);
    }
}
