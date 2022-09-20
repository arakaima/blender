package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.InspectorDossier;
import be.fgov.famhp.imt.backoffice.service.dto.InspectorDossierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectorDossier} and its DTO {@link InspectorDossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectorDossierMapper extends EntityMapper<InspectorDossierDTO, InspectorDossier> {}
