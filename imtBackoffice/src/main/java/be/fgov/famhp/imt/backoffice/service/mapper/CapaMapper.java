package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import be.fgov.famhp.imt.backoffice.service.dto.CapaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Capa} and its DTO {@link CapaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CapaMapper extends EntityMapper<CapaDTO, Capa> {}
