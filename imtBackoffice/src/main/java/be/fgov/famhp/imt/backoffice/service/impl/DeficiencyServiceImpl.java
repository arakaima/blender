package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Deficiency;
import be.fgov.famhp.imt.backoffice.repository.DeficiencyRepository;
import be.fgov.famhp.imt.backoffice.service.DeficiencyService;
import be.fgov.famhp.imt.backoffice.service.dto.DeficiencyDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.DeficiencyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Deficiency}.
 */
@Service
public class DeficiencyServiceImpl implements DeficiencyService {

    private final Logger log = LoggerFactory.getLogger(DeficiencyServiceImpl.class);

    private final DeficiencyRepository deficiencyRepository;

    private final DeficiencyMapper deficiencyMapper;

    public DeficiencyServiceImpl(DeficiencyRepository deficiencyRepository, DeficiencyMapper deficiencyMapper) {
        this.deficiencyRepository = deficiencyRepository;
        this.deficiencyMapper = deficiencyMapper;
    }

    @Override
    public DeficiencyDTO save(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to save Deficiency : {}", deficiencyDTO);
        Deficiency deficiency = deficiencyMapper.toEntity(deficiencyDTO);
        deficiency = deficiencyRepository.save(deficiency);
        return deficiencyMapper.toDto(deficiency);
    }

    @Override
    public DeficiencyDTO update(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to update Deficiency : {}", deficiencyDTO);
        Deficiency deficiency = deficiencyMapper.toEntity(deficiencyDTO);
        // no save call needed as we have no fields that can be updated
        return deficiencyMapper.toDto(deficiency);
    }

    @Override
    public Optional<DeficiencyDTO> partialUpdate(DeficiencyDTO deficiencyDTO) {
        log.debug("Request to partially update Deficiency : {}", deficiencyDTO);

        return deficiencyRepository
            .findById(deficiencyDTO.getId())
            .map(existingDeficiency -> {
                deficiencyMapper.partialUpdate(existingDeficiency, deficiencyDTO);

                return existingDeficiency;
            })
            // .map(deficiencyRepository::save)
            .map(deficiencyMapper::toDto);
    }

    @Override
    public List<DeficiencyDTO> findAll() {
        log.debug("Request to get all Deficiencies");
        return deficiencyRepository.findAll().stream().map(deficiencyMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<DeficiencyDTO> findOne(String id) {
        log.debug("Request to get Deficiency : {}", id);
        return deficiencyRepository.findById(id).map(deficiencyMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Deficiency : {}", id);
        deficiencyRepository.deleteById(id);
    }
}
