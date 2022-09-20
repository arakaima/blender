package be.fgov.famhp.imt.backoffice.service.mapper;

import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectionReport} and its DTO {@link InspectionReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionReportMapper extends EntityMapper<InspectionReportDTO, InspectionReport> {}
