package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Site;
import be.fgov.famhp.imt.backoffice.repository.SiteRepository;
import be.fgov.famhp.imt.backoffice.service.SiteService;
import java.util.List;
import java.util.Optional;
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

    public SiteServiceImpl(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    @Override
    public Site save(Site site) {
        log.debug("Request to save Site : {}", site);
        return siteRepository.save(site);
    }

    @Override
    public Site update(Site site) {
        log.debug("Request to update Site : {}", site);
        return siteRepository.save(site);
    }

    @Override
    public Optional<Site> partialUpdate(Site site) {
        log.debug("Request to partially update Site : {}", site);

        return siteRepository
            .findById(site.getId())
            .map(existingSite -> {
                if (site.getActivityId() != null) {
                    existingSite.setActivityId(site.getActivityId());
                }
                if (site.getActivityName() != null) {
                    existingSite.setActivityName(site.getActivityName());
                }
                if (site.getStartDate() != null) {
                    existingSite.setStartDate(site.getStartDate());
                }
                if (site.getLabel() != null) {
                    existingSite.setLabel(site.getLabel());
                }

                return existingSite;
            })
            .map(siteRepository::save);
    }

    @Override
    public List<Site> findAll() {
        log.debug("Request to get all Sites");
        return siteRepository.findAll();
    }

    @Override
    public Optional<Site> findOne(String id) {
        log.debug("Request to get Site : {}", id);
        return siteRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Site : {}", id);
        siteRepository.deleteById(id);
    }
}
