package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Expert;
import be.fgov.famhp.imt.backoffice.repository.ExpertRepository;
import be.fgov.famhp.imt.backoffice.service.ExpertService;
import be.fgov.famhp.imt.backoffice.service.dto.ExpertDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.ExpertMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Expert}.
 */
@Service
public class ExpertServiceImpl implements ExpertService {

    private final Logger log = LoggerFactory.getLogger(ExpertServiceImpl.class);

    private final ExpertRepository expertRepository;

    private final ExpertMapper expertMapper;

    public ExpertServiceImpl(ExpertRepository expertRepository, ExpertMapper expertMapper) {
        this.expertRepository = expertRepository;
        this.expertMapper = expertMapper;
    }

    @Override
    public ExpertDTO save(ExpertDTO expertDTO) {
        log.debug("Request to save Expert : {}", expertDTO);
        Expert expert = expertMapper.toEntity(expertDTO);
        expert = expertRepository.save(expert);
        return expertMapper.toDto(expert);
    }

    @Override
    public ExpertDTO update(ExpertDTO expertDTO) {
        log.debug("Request to update Expert : {}", expertDTO);
        Expert expert = expertMapper.toEntity(expertDTO);
        // no save call needed as we have no fields that can be updated
        return expertMapper.toDto(expert);
    }

    @Override
    public Optional<ExpertDTO> partialUpdate(ExpertDTO expertDTO) {
        log.debug("Request to partially update Expert : {}", expertDTO);

        return expertRepository
            .findById(expertDTO.getId())
            .map(existingExpert -> {
                expertMapper.partialUpdate(existingExpert, expertDTO);

                return existingExpert;
            })
            // .map(expertRepository::save)
            .map(expertMapper::toDto);
    }

    @Override
    public List<ExpertDTO> findAll() {
        log.debug("Request to get all Experts");
        return expertRepository.findAll().stream().map(expertMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ExpertDTO> findOne(String id) {
        log.debug("Request to get Expert : {}", id);
        return expertRepository.findById(id).map(expertMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Expert : {}", id);
        expertRepository.deleteById(id);
    }
}
