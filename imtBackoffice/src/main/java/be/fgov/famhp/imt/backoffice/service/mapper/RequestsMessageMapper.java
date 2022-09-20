package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestsMessage} and its DTO {@link RequestsMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestsMessageMapper extends EntityMapper<RequestsMessageDTO, RequestsMessage> {}
