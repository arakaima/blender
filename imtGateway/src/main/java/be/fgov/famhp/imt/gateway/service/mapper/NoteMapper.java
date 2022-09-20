package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.Note;
import be.fgov.famhp.imt.gateway.service.dto.NoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring")
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {}
