package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.DossierType;
import be.fgov.famhp.imt.gateway.service.dto.DossierTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DossierType} and its DTO {@link DossierTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierTypeMapper extends EntityMapper<DossierTypeDTO, DossierType> {}
