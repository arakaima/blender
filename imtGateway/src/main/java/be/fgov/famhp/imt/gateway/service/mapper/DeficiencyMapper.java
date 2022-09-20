package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Deficiency;
import be.fgov.famhp.imt.gateway.service.dto.DeficiencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deficiency} and its DTO {@link DeficiencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeficiencyMapper extends EntityMapper<DeficiencyDTO, Deficiency> {}
