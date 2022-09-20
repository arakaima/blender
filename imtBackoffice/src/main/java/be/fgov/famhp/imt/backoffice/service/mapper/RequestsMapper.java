package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Requests} and its DTO {@link RequestsDTO}.
 */
@Mapper(componentModel = "spring")
public interface RequestsMapper extends EntityMapper<RequestsDTO, Requests> {}
