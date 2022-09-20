package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.ContactPerson;
import be.fgov.famhp.imt.gateway.service.dto.ContactPersonDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContactPerson} and its DTO {@link ContactPersonDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContactPersonMapper extends EntityMapper<ContactPersonDTO, ContactPerson> {}
