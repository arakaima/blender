package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Requests;
import be.fgov.famhp.imt.gateway.service.dto.RequestsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Requests} and its DTO {@link RequestsDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestsMapper extends EntityMapper<RequestsDTO, Requests> {}
