package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Capa;
import be.fgov.famhp.imt.backoffice.repository.CapaRepository;
import be.fgov.famhp.imt.backoffice.service.CapaService;
import java.util.List;
import java.util.Optional;
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

    public CapaServiceImpl(CapaRepository capaRepository) {
        this.capaRepository = capaRepository;
    }

    @Override
    public Capa save(Capa capa) {
        log.debug("Request to save Capa : {}", capa);
        return capaRepository.save(capa);
    }

    @Override
    public Capa update(Capa capa) {
        log.debug("Request to update Capa : {}", capa);
        // no save call needed as we have no fields that can be updated
        return capa;
    }

    @Override
    public Optional<Capa> partialUpdate(Capa capa) {
        log.debug("Request to partially update Capa : {}", capa);

        return capaRepository
            .findById(capa.getId())
            .map(existingCapa -> {
                return existingCapa;
            })// .map(capaRepository::save)
        ;
    }

    @Override
    public List<Capa> findAll() {
        log.debug("Request to get all Capas");
        return capaRepository.findAll();
    }

    @Override
    public Optional<Capa> findOne(String id) {
        log.debug("Request to get Capa : {}", id);
        return capaRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Capa : {}", id);
        capaRepository.deleteById(id);
    }
}
