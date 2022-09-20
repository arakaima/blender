package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Dossier;
import be.fgov.famhp.imt.gateway.service.dto.DossierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dossier} and its DTO {@link DossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierMapper extends EntityMapper<DossierDTO, Dossier> {}
