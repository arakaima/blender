package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Location;
import be.fgov.famhp.imt.gateway.service.dto.LocationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {}
