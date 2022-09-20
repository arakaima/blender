package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.OrganizationDocument;
import be.fgov.famhp.imt.gateway.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.gateway.service.OrganizationDocumentService;
import be.fgov.famhp.imt.gateway.service.dto.OrganizationDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.OrganizationDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link OrganizationDocument}.
 */
@Service
public class OrganizationDocumentServiceImpl implements OrganizationDocumentService {

    private final Logger log = LoggerFactory.getLogger(OrganizationDocumentServiceImpl.class);

    private final OrganizationDocumentRepository organizationDocumentRepository;

    private final OrganizationDocumentMapper organizationDocumentMapper;

    public OrganizationDocumentServiceImpl(
        OrganizationDocumentRepository organizationDocumentRepository,
        OrganizationDocumentMapper organizationDocumentMapper
    ) {
        this.organizationDocumentRepository = organizationDocumentRepository;
        this.organizationDocumentMapper = organizationDocumentMapper;
    }

    @Override
    public Mono<OrganizationDocumentDTO> save(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to save OrganizationDocument : {}", organizationDocumentDTO);
        return organizationDocumentRepository
            .save(organizationDocumentMapper.toEntity(organizationDocumentDTO))
            .map(organizationDocumentMapper::toDto);
    }

    @Override
    public Mono<OrganizationDocumentDTO> update(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to update OrganizationDocument : {}", organizationDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return organizationDocumentRepository.findById(organizationDocumentDTO.getId()).map(organizationDocumentMapper::toDto);
    }

    @Override
    public Mono<OrganizationDocumentDTO> partialUpdate(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to partially update OrganizationDocument : {}", organizationDocumentDTO);

        return organizationDocumentRepository
            .findById(organizationDocumentDTO.getId())
            .map(existingOrganizationDocument -> {
                organizationDocumentMapper.partialUpdate(existingOrganizationDocument, organizationDocumentDTO);

                return existingOrganizationDocument;
            })
            // .flatMap(organizationDocumentRepository::save)
            .map(organizationDocumentMapper::toDto);
    }

    @Override
    public Flux<OrganizationDocumentDTO> findAll() {
        log.debug("Request to get all OrganizationDocuments");
        return organizationDocumentRepository.findAll().map(organizationDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return organizationDocumentRepository.count();
    }

    @Override
    public Mono<OrganizationDocumentDTO> findOne(String id) {
        log.debug("Request to get OrganizationDocument : {}", id);
        return organizationDocumentRepository.findById(id).map(organizationDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete OrganizationDocument : {}", id);
        return organizationDocumentRepository.deleteById(id);
    }
}
