package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.Activity;
import be.fgov.famhp.imt.backoffice.service.dto.ActivityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {}
