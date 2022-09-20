package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.AttachedDocument;
import be.fgov.famhp.imt.gateway.service.dto.AttachedDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AttachedDocument} and its DTO {@link AttachedDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttachedDocumentMapper extends EntityMapper<AttachedDocumentDTO, AttachedDocument> {}
