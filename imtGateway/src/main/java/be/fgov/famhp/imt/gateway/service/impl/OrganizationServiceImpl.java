package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Organization;
import be.fgov.famhp.imt.gateway.repository.OrganizationRepository;
import be.fgov.famhp.imt.gateway.service.OrganizationService;
import be.fgov.famhp.imt.gateway.service.dto.OrganizationDTO;
import be.fgov.famhp.imt.gateway.service.mapper.OrganizationMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public Mono<OrganizationDTO> save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        return organizationRepository.save(organizationMapper.toEntity(organizationDTO)).map(organizationMapper::toDto);
    }

    @Override
    public Mono<OrganizationDTO> update(OrganizationDTO organizationDTO) {
        log.debug("Request to update Organization : {}", organizationDTO);
        // no save call needed as we have no fields that can be updated
        return organizationRepository.findById(organizationDTO.getId()).map(organizationMapper::toDto);
    }

    @Override
    public Mono<OrganizationDTO> partialUpdate(OrganizationDTO organizationDTO) {
        log.debug("Request to partially update Organization : {}", organizationDTO);

        return organizationRepository
            .findById(organizationDTO.getId())
            .map(existingOrganization -> {
                organizationMapper.partialUpdate(existingOrganization, organizationDTO);

                return existingOrganization;
            })
            // .flatMap(organizationRepository::save)
            .map(organizationMapper::toDto);
    }

    @Override
    public Flux<OrganizationDTO> findAll() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll().map(organizationMapper::toDto);
    }

    public Mono<Long> countAll() {
        return organizationRepository.count();
    }

    @Override
    public Mono<OrganizationDTO> findOne(String id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id).map(organizationMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Organization : {}", id);
        return organizationRepository.deleteById(id);
    }
}
