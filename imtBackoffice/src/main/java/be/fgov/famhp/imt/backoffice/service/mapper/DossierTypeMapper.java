package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.DossierType;
import be.fgov.famhp.imt.backoffice.service.dto.DossierTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DossierType} and its DTO {@link DossierTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierTypeMapper extends EntityMapper<DossierTypeDTO, DossierType> {}
