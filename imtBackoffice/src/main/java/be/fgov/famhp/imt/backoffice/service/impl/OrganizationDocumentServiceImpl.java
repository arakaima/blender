package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import be.fgov.famhp.imt.backoffice.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.OrganizationDocumentService;
import be.fgov.famhp.imt.backoffice.service.dto.OrganizationDocumentDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.OrganizationDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public OrganizationDocumentDTO save(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to save OrganizationDocument : {}", organizationDocumentDTO);
        OrganizationDocument organizationDocument = organizationDocumentMapper.toEntity(organizationDocumentDTO);
        organizationDocument = organizationDocumentRepository.save(organizationDocument);
        return organizationDocumentMapper.toDto(organizationDocument);
    }

    @Override
    public OrganizationDocumentDTO update(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to update OrganizationDocument : {}", organizationDocumentDTO);
        OrganizationDocument organizationDocument = organizationDocumentMapper.toEntity(organizationDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return organizationDocumentMapper.toDto(organizationDocument);
    }

    @Override
    public Optional<OrganizationDocumentDTO> partialUpdate(OrganizationDocumentDTO organizationDocumentDTO) {
        log.debug("Request to partially update OrganizationDocument : {}", organizationDocumentDTO);

        return organizationDocumentRepository
            .findById(organizationDocumentDTO.getId())
            .map(existingOrganizationDocument -> {
                organizationDocumentMapper.partialUpdate(existingOrganizationDocument, organizationDocumentDTO);

                return existingOrganizationDocument;
            })
            // .map(organizationDocumentRepository::save)
            .map(organizationDocumentMapper::toDto);
    }

    @Override
    public List<OrganizationDocumentDTO> findAll() {
        log.debug("Request to get all OrganizationDocuments");
        return organizationDocumentRepository
            .findAll()
            .stream()
            .map(organizationDocumentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<OrganizationDocumentDTO> findOne(String id) {
        log.debug("Request to get OrganizationDocument : {}", id);
        return organizationDocumentRepository.findById(id).map(organizationDocumentMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete OrganizationDocument : {}", id);
        organizationDocumentRepository.deleteById(id);
    }
}
