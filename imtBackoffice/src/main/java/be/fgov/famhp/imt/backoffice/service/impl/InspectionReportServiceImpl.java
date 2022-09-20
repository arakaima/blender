package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.InspectionReport;
import be.fgov.famhp.imt.backoffice.repository.InspectionReportRepository;
import be.fgov.famhp.imt.backoffice.service.InspectionReportService;
import be.fgov.famhp.imt.backoffice.service.dto.InspectionReportDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.InspectionReportMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link InspectionReport}.
 */
@Service
public class InspectionReportServiceImpl implements InspectionReportService {

    private final Logger log = LoggerFactory.getLogger(InspectionReportServiceImpl.class);

    private final InspectionReportRepository inspectionReportRepository;

    private final InspectionReportMapper inspectionReportMapper;

    public InspectionReportServiceImpl(
        InspectionReportRepository inspectionReportRepository,
        InspectionReportMapper inspectionReportMapper
    ) {
        this.inspectionReportRepository = inspectionReportRepository;
        this.inspectionReportMapper = inspectionReportMapper;
    }

    @Override
    public InspectionReportDTO save(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to save InspectionReport : {}", inspectionReportDTO);
        InspectionReport inspectionReport = inspectionReportMapper.toEntity(inspectionReportDTO);
        inspectionReport = inspectionReportRepository.save(inspectionReport);
        return inspectionReportMapper.toDto(inspectionReport);
    }

    @Override
    public InspectionReportDTO update(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to update InspectionReport : {}", inspectionReportDTO);
        InspectionReport inspectionReport = inspectionReportMapper.toEntity(inspectionReportDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionReportMapper.toDto(inspectionReport);
    }

    @Override
    public Optional<InspectionReportDTO> partialUpdate(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to partially update InspectionReport : {}", inspectionReportDTO);

        return inspectionReportRepository
            .findById(inspectionReportDTO.getId())
            .map(existingInspectionReport -> {
                inspectionReportMapper.partialUpdate(existingInspectionReport, inspectionReportDTO);

                return existingInspectionReport;
            })
            // .map(inspectionReportRepository::save)
            .map(inspectionReportMapper::toDto);
    }

    @Override
    public List<InspectionReportDTO> findAll() {
        log.debug("Request to get all InspectionReports");
        return inspectionReportRepository
            .findAll()
            .stream()
            .map(inspectionReportMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<InspectionReportDTO> findOne(String id) {
        log.debug("Request to get InspectionReport : {}", id);
        return inspectionReportRepository.findById(id).map(inspectionReportMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete InspectionReport : {}", id);
        inspectionReportRepository.deleteById(id);
    }
}
