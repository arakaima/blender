package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.OrganizationDocument;
import be.fgov.famhp.imt.backoffice.repository.OrganizationDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.OrganizationDocumentService;
import java.util.List;
import java.util.Optional;
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

    public OrganizationDocumentServiceImpl(OrganizationDocumentRepository organizationDocumentRepository) {
        this.organizationDocumentRepository = organizationDocumentRepository;
    }

    @Override
    public OrganizationDocument save(OrganizationDocument organizationDocument) {
        log.debug("Request to save OrganizationDocument : {}", organizationDocument);
        return organizationDocumentRepository.save(organizationDocument);
    }

    @Override
    public OrganizationDocument update(OrganizationDocument organizationDocument) {
        log.debug("Request to update OrganizationDocument : {}", organizationDocument);
        return organizationDocumentRepository.save(organizationDocument);
    }

    @Override
    public Optional<OrganizationDocument> partialUpdate(OrganizationDocument organizationDocument) {
        log.debug("Request to partially update OrganizationDocument : {}", organizationDocument);

        return organizationDocumentRepository
            .findById(organizationDocument.getId())
            .map(existingOrganizationDocument -> {
                if (organizationDocument.getDocumentName() != null) {
                    existingOrganizationDocument.setDocumentName(organizationDocument.getDocumentName());
                }
                if (organizationDocument.getDocumentTitle() != null) {
                    existingOrganizationDocument.setDocumentTitle(organizationDocument.getDocumentTitle());
                }
                if (organizationDocument.getDocumentType() != null) {
                    existingOrganizationDocument.setDocumentType(organizationDocument.getDocumentType());
                }
                if (organizationDocument.getStatus() != null) {
                    existingOrganizationDocument.setStatus(organizationDocument.getStatus());
                }

                return existingOrganizationDocument;
            })
            .map(organizationDocumentRepository::save);
    }

    @Override
    public List<OrganizationDocument> findAll() {
        log.debug("Request to get all OrganizationDocuments");
        return organizationDocumentRepository.findAll();
    }

    @Override
    public Optional<OrganizationDocument> findOne(String id) {
        log.debug("Request to get OrganizationDocument : {}", id);
        return organizationDocumentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete OrganizationDocument : {}", id);
        organizationDocumentRepository.deleteById(id);
    }
}
