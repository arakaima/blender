package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import be.fgov.famhp.imt.backoffice.service.dto.AttachedDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttachedDocument} and its DTO {@link AttachedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachedDocumentMapper extends EntityMapper<AttachedDocumentDTO, AttachedDocument> {}
