package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.RequestsMessage;
import be.fgov.famhp.imt.gateway.service.dto.RequestsMessageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RequestsMessage} and its DTO {@link RequestsMessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestsMessageMapper extends EntityMapper<RequestsMessageDTO, RequestsMessage> {}
