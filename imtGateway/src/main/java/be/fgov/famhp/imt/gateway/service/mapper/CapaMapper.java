package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Capa;
import be.fgov.famhp.imt.gateway.service.dto.CapaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Capa} and its DTO {@link CapaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CapaMapper extends EntityMapper<CapaDTO, Capa> {}
