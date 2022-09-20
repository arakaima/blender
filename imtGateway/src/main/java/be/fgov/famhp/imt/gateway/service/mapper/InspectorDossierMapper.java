package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.InspectorDossier;
import be.fgov.famhp.imt.gateway.service.dto.InspectorDossierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectorDossier} and its DTO {@link InspectorDossierDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectorDossierMapper extends EntityMapper<InspectorDossierDTO, InspectorDossier> {}
