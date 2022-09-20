package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.CapaDocument;
import be.fgov.famhp.imt.gateway.service.dto.CapaDocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CapaDocument} and its DTO {@link CapaDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CapaDocumentMapper extends EntityMapper<CapaDocumentDTO, CapaDocument> {}
