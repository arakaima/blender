package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.DossierStatus;
import be.fgov.famhp.imt.gateway.service.dto.DossierStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DossierStatus} and its DTO {@link DossierStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierStatusMapper extends EntityMapper<DossierStatusDTO, DossierStatus> {}
