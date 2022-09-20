package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Dossier;
import be.fgov.famhp.imt.backoffice.repository.DossierRepository;
import be.fgov.famhp.imt.backoffice.service.DossierService;
import be.fgov.famhp.imt.backoffice.service.dto.DossierDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DossierMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
public class DossierServiceImpl implements DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierServiceImpl.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;

    public DossierServiceImpl(DossierRepository dossierRepository, DossierMapper dossierMapper) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
    }

    @Override
    public DossierDTO save(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        dossier = dossierRepository.save(dossier);
        return dossierMapper.toDto(dossier);
    }

    @Override
    public DossierDTO update(DossierDTO dossierDTO) {
        log.debug("Request to update Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        // no save call needed as we have no fields that can be updated
        return dossierMapper.toDto(dossier);
    }

    @Override
    public Optional<DossierDTO> partialUpdate(DossierDTO dossierDTO) {
        log.debug("Request to partially update Dossier : {}", dossierDTO);

        return dossierRepository
            .findById(dossierDTO.getId())
            .map(existingDossier -> {
                dossierMapper.partialUpdate(existingDossier, dossierDTO);

                return existingDossier;
            })
            // .map(dossierRepository::save)
            .map(dossierMapper::toDto);
    }

    @Override
    public List<DossierDTO> findAll() {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll().stream().map(dossierMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<DossierDTO> findOne(String id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id).map(dossierMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
    }
}
