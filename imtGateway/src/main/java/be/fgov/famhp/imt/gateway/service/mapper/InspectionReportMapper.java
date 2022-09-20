package be.fgov.famhp.imt.gateway.service.mapper;

import be.fgov.famhp.imt.gateway.domain.InspectionReport;
import be.fgov.famhp.imt.gateway.service.dto.InspectionReportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link InspectionReport} and its DTO {@link InspectionReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface InspectionReportMapper extends EntityMapper<InspectionReportDTO, InspectionReport> {}
