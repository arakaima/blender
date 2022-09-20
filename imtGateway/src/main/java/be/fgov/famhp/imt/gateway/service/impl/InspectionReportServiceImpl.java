package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.InspectionReport;
import be.fgov.famhp.imt.gateway.repository.InspectionReportRepository;
import be.fgov.famhp.imt.gateway.service.InspectionReportService;
import be.fgov.famhp.imt.gateway.service.dto.InspectionReportDTO;
import be.fgov.famhp.imt.gateway.service.mapper.InspectionReportMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<InspectionReportDTO> save(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to save InspectionReport : {}", inspectionReportDTO);
        return inspectionReportRepository.save(inspectionReportMapper.toEntity(inspectionReportDTO)).map(inspectionReportMapper::toDto);
    }

    @Override
    public Mono<InspectionReportDTO> update(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to update InspectionReport : {}", inspectionReportDTO);
        // no save call needed as we have no fields that can be updated
        return inspectionReportRepository.findById(inspectionReportDTO.getId()).map(inspectionReportMapper::toDto);
    }

    @Override
    public Mono<InspectionReportDTO> partialUpdate(InspectionReportDTO inspectionReportDTO) {
        log.debug("Request to partially update InspectionReport : {}", inspectionReportDTO);

        return inspectionReportRepository
            .findById(inspectionReportDTO.getId())
            .map(existingInspectionReport -> {
                inspectionReportMapper.partialUpdate(existingInspectionReport, inspectionReportDTO);

                return existingInspectionReport;
            })
            // .flatMap(inspectionReportRepository::save)
            .map(inspectionReportMapper::toDto);
    }

    @Override
    public Flux<InspectionReportDTO> findAll() {
        log.debug("Request to get all InspectionReports");
        return inspectionReportRepository.findAll().map(inspectionReportMapper::toDto);
    }

    public Mono<Long> countAll() {
        return inspectionReportRepository.count();
    }

    @Override
    public Mono<InspectionReportDTO> findOne(String id) {
        log.debug("Request to get InspectionReport : {}", id);
        return inspectionReportRepository.findById(id).map(inspectionReportMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete InspectionReport : {}", id);
        return inspectionReportRepository.deleteById(id);
    }
}
