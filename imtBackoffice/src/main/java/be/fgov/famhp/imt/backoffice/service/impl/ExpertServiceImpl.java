package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Expert;
import be.fgov.famhp.imt.backoffice.repository.ExpertRepository;
import be.fgov.famhp.imt.backoffice.service.ExpertService;
import java.util.List;
import java.util.Optional;
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

    public ExpertServiceImpl(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    @Override
    public Expert save(Expert expert) {
        log.debug("Request to save Expert : {}", expert);
        return expertRepository.save(expert);
    }

    @Override
    public Expert update(Expert expert) {
        log.debug("Request to update Expert : {}", expert);
        // no save call needed as we have no fields that can be updated
        return expert;
    }

    @Override
    public Optional<Expert> partialUpdate(Expert expert) {
        log.debug("Request to partially update Expert : {}", expert);

        return expertRepository
            .findById(expert.getId())
            .map(existingExpert -> {
                return existingExpert;
            })// .map(expertRepository::save)
        ;
    }

    @Override
    public List<Expert> findAll() {
        log.debug("Request to get all Experts");
        return expertRepository.findAll();
    }

    @Override
    public Optional<Expert> findOne(String id) {
        log.debug("Request to get Expert : {}", id);
        return expertRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Expert : {}", id);
        expertRepository.deleteById(id);
    }
}
