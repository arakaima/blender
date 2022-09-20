package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Site;
import be.fgov.famhp.imt.gateway.repository.SiteRepository;
import be.fgov.famhp.imt.gateway.service.SiteService;
import be.fgov.famhp.imt.gateway.service.dto.SiteDTO;
import be.fgov.famhp.imt.gateway.service.mapper.SiteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Site}.
 */
@Service
public class SiteServiceImpl implements SiteService {

    private final Logger log = LoggerFactory.getLogger(SiteServiceImpl.class);

    private final SiteRepository siteRepository;

    private final SiteMapper siteMapper;

    public SiteServiceImpl(SiteRepository siteRepository, SiteMapper siteMapper) {
        this.siteRepository = siteRepository;
        this.siteMapper = siteMapper;
    }

    @Override
    public Mono<SiteDTO> save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);
        return siteRepository.save(siteMapper.toEntity(siteDTO)).map(siteMapper::toDto);
    }

    @Override
    public Mono<SiteDTO> update(SiteDTO siteDTO) {
        log.debug("Request to update Site : {}", siteDTO);
        // no save call needed as we have no fields that can be updated
        return siteRepository.findById(siteDTO.getId()).map(siteMapper::toDto);
    }

    @Override
    public Mono<SiteDTO> partialUpdate(SiteDTO siteDTO) {
        log.debug("Request to partially update Site : {}", siteDTO);

        return siteRepository
            .findById(siteDTO.getId())
            .map(existingSite -> {
                siteMapper.partialUpdate(existingSite, siteDTO);

                return existingSite;
            })
            // .flatMap(siteRepository::save)
            .map(siteMapper::toDto);
    }

    @Override
    public Flux<SiteDTO> findAll() {
        log.debug("Request to get all Sites");
        return siteRepository.findAll().map(siteMapper::toDto);
    }

    public Mono<Long> countAll() {
        return siteRepository.count();
    }

    @Override
    public Mono<SiteDTO> findOne(String id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findById(id).map(siteMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Site : {}", id);
        return siteRepository.deleteById(id);
    }
}
