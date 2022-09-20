package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import be.fgov.famhp.imt.backoffice.service.dto.OrganizationDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationDocument} and its DTO {@link OrganizationDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrganizationDocumentMapper extends EntityMapper<OrganizationDocumentDTO, OrganizationDocument> {}
