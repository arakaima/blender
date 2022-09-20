package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Deficiency;
import be.fgov.famhp.imt.backoffice.service.dto.DeficiencyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deficiency} and its DTO {@link DeficiencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeficiencyMapper extends EntityMapper<DeficiencyDTO, Deficiency> {}
