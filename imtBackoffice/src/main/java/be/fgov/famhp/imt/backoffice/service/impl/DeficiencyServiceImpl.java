package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Deficiency;
import be.fgov.famhp.imt.backoffice.repository.DeficiencyRepository;
import be.fgov.famhp.imt.backoffice.service.DeficiencyService;
import java.util.List;
import java.util.Optional;
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

    public DeficiencyServiceImpl(DeficiencyRepository deficiencyRepository) {
        this.deficiencyRepository = deficiencyRepository;
    }

    @Override
    public Deficiency save(Deficiency deficiency) {
        log.debug("Request to save Deficiency : {}", deficiency);
        return deficiencyRepository.save(deficiency);
    }

    @Override
    public Deficiency update(Deficiency deficiency) {
        log.debug("Request to update Deficiency : {}", deficiency);
        // no save call needed as we have no fields that can be updated
        return deficiency;
    }

    @Override
    public Optional<Deficiency> partialUpdate(Deficiency deficiency) {
        log.debug("Request to partially update Deficiency : {}", deficiency);

        return deficiencyRepository
            .findById(deficiency.getId())
            .map(existingDeficiency -> {
                return existingDeficiency;
            })// .map(deficiencyRepository::save)
        ;
    }

    @Override
    public List<Deficiency> findAll() {
        log.debug("Request to get all Deficiencies");
        return deficiencyRepository.findAll();
    }

    @Override
    public Optional<Deficiency> findOne(String id) {
        log.debug("Request to get Deficiency : {}", id);
        return deficiencyRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Deficiency : {}", id);
        deficiencyRepository.deleteById(id);
    }
}
