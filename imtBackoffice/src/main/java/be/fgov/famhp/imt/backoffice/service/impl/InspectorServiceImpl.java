package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Inspector;
import be.fgov.famhp.imt.backoffice.repository.InspectorRepository;
import be.fgov.famhp.imt.backoffice.service.InspectorService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Inspector}.
 */
@Service
public class InspectorServiceImpl implements InspectorService {

    private final Logger log = LoggerFactory.getLogger(InspectorServiceImpl.class);

    private final InspectorRepository inspectorRepository;

    public InspectorServiceImpl(InspectorRepository inspectorRepository) {
        this.inspectorRepository = inspectorRepository;
    }

    @Override
    public Inspector save(Inspector inspector) {
        log.debug("Request to save Inspector : {}", inspector);
        return inspectorRepository.save(inspector);
    }

    @Override
    public Inspector update(Inspector inspector) {
        log.debug("Request to update Inspector : {}", inspector);
        // no save call needed as we have no fields that can be updated
        return inspector;
    }

    @Override
    public Optional<Inspector> partialUpdate(Inspector inspector) {
        log.debug("Request to partially update Inspector : {}", inspector);

        return inspectorRepository
            .findById(inspector.getId())
            .map(existingInspector -> {
                return existingInspector;
            })// .map(inspectorRepository::save)
        ;
    }

    @Override
    public List<Inspector> findAll() {
        log.debug("Request to get all Inspectors");
        return inspectorRepository.findAll();
    }

    @Override
    public Optional<Inspector> findOne(String id) {
        log.debug("Request to get Inspector : {}", id);
        return inspectorRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Inspector : {}", id);
        inspectorRepository.deleteById(id);
    }
}
