package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import be.fgov.famhp.imt.backoffice.repository.CapaRepository;
import be.fgov.famhp.imt.backoffice.service.CapaService;
import be.fgov.famhp.imt.backoffice.service.dto.CapaDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.CapaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Capa}.
 */
@Service
public class CapaServiceImpl implements CapaService {

    private final Logger log = LoggerFactory.getLogger(CapaServiceImpl.class);

    private final CapaRepository capaRepository;

    private final CapaMapper capaMapper;

    public CapaServiceImpl(CapaRepository capaRepository, CapaMapper capaMapper) {
        this.capaRepository = capaRepository;
        this.capaMapper = capaMapper;
    }

    @Override
    public CapaDTO save(CapaDTO capaDTO) {
        log.debug("Request to save Capa : {}", capaDTO);
        Capa capa = capaMapper.toEntity(capaDTO);
        capa = capaRepository.save(capa);
        return capaMapper.toDto(capa);
    }

    @Override
    public CapaDTO update(CapaDTO capaDTO) {
        log.debug("Request to update Capa : {}", capaDTO);
        Capa capa = capaMapper.toEntity(capaDTO);
        // no save call needed as we have no fields that can be updated
        return capaMapper.toDto(capa);
    }

    @Override
    public Optional<CapaDTO> partialUpdate(CapaDTO capaDTO) {
        log.debug("Request to partially update Capa : {}", capaDTO);

        return capaRepository
            .findById(capaDTO.getId())
            .map(existingCapa -> {
                capaMapper.partialUpdate(existingCapa, capaDTO);

                return existingCapa;
            })
            // .map(capaRepository::save)
            .map(capaMapper::toDto);
    }

    @Override
    public List<CapaDTO> findAll() {
        log.debug("Request to get all Capas");
        return capaRepository.findAll().stream().map(capaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<CapaDTO> findOne(String id) {
        log.debug("Request to get Capa : {}", id);
        return capaRepository.findById(id).map(capaMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Capa : {}", id);
        capaRepository.deleteById(id);
    }
}
