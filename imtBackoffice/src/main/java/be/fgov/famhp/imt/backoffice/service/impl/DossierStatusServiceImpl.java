package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.DossierStatus;
import be.fgov.famhp.imt.backoffice.repository.DossierStatusRepository;
import be.fgov.famhp.imt.backoffice.service.DossierStatusService;
import be.fgov.famhp.imt.backoffice.service.dto.DossierStatusDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DossierStatusMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link DossierStatus}.
 */
@Service
public class DossierStatusServiceImpl implements DossierStatusService {

    private final Logger log = LoggerFactory.getLogger(DossierStatusServiceImpl.class);

    private final DossierStatusRepository dossierStatusRepository;

    private final DossierStatusMapper dossierStatusMapper;

    public DossierStatusServiceImpl(DossierStatusRepository dossierStatusRepository, DossierStatusMapper dossierStatusMapper) {
        this.dossierStatusRepository = dossierStatusRepository;
        this.dossierStatusMapper = dossierStatusMapper;
    }

    @Override
    public DossierStatusDTO save(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to save DossierStatus : {}", dossierStatusDTO);
        DossierStatus dossierStatus = dossierStatusMapper.toEntity(dossierStatusDTO);
        dossierStatus = dossierStatusRepository.save(dossierStatus);
        return dossierStatusMapper.toDto(dossierStatus);
    }

    @Override
    public DossierStatusDTO update(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to update DossierStatus : {}", dossierStatusDTO);
        DossierStatus dossierStatus = dossierStatusMapper.toEntity(dossierStatusDTO);
        // no save call needed as we have no fields that can be updated
        return dossierStatusMapper.toDto(dossierStatus);
    }

    @Override
    public Optional<DossierStatusDTO> partialUpdate(DossierStatusDTO dossierStatusDTO) {
        log.debug("Request to partially update DossierStatus : {}", dossierStatusDTO);

        return dossierStatusRepository
            .findById(dossierStatusDTO.getId())
            .map(existingDossierStatus -> {
                dossierStatusMapper.partialUpdate(existingDossierStatus, dossierStatusDTO);

                return existingDossierStatus;
            })
            // .map(dossierStatusRepository::save)
            .map(dossierStatusMapper::toDto);
    }

    @Override
    public List<DossierStatusDTO> findAll() {
        log.debug("Request to get all DossierStatuses");
        return dossierStatusRepository.findAll().stream().map(dossierStatusMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<DossierStatusDTO> findOne(String id) {
        log.debug("Request to get DossierStatus : {}", id);
        return dossierStatusRepository.findById(id).map(dossierStatusMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete DossierStatus : {}", id);
        dossierStatusRepository.deleteById(id);
    }
}
