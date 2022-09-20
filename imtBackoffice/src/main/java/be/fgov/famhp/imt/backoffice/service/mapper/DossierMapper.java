package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Dossier;
import be.fgov.famhp.imt.backoffice.service.dto.DossierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dossier} and its DTO {@link DossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface DossierMapper extends EntityMapper<DossierDTO, Dossier> {}
