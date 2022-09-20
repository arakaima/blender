package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Site;
import be.fgov.famhp.imt.backoffice.repository.SiteRepository;
import be.fgov.famhp.imt.backoffice.service.SiteService;
import be.fgov.famhp.imt.backoffice.service.dto.SiteDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.SiteMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public SiteDTO save(SiteDTO siteDTO) {
        log.debug("Request to save Site : {}", siteDTO);
        Site site = siteMapper.toEntity(siteDTO);
        site = siteRepository.save(site);
        return siteMapper.toDto(site);
    }

    @Override
    public SiteDTO update(SiteDTO siteDTO) {
        log.debug("Request to update Site : {}", siteDTO);
        Site site = siteMapper.toEntity(siteDTO);
        // no save call needed as we have no fields that can be updated
        return siteMapper.toDto(site);
    }

    @Override
    public Optional<SiteDTO> partialUpdate(SiteDTO siteDTO) {
        log.debug("Request to partially update Site : {}", siteDTO);

        return siteRepository
            .findById(siteDTO.getId())
            .map(existingSite -> {
                siteMapper.partialUpdate(existingSite, siteDTO);

                return existingSite;
            })
            // .map(siteRepository::save)
            .map(siteMapper::toDto);
    }

    @Override
    public List<SiteDTO> findAll() {
        log.debug("Request to get all Sites");
        return siteRepository.findAll().stream().map(siteMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<SiteDTO> findOne(String id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findById(id).map(siteMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.deleteById(id);
    }
}
