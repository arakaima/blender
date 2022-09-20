package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import be.fgov.famhp.imt.backoffice.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.AttachedDocumentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link AttachedDocument}.
 */
@Service
public class AttachedDocumentServiceImpl implements AttachedDocumentService {

    private final Logger log = LoggerFactory.getLogger(AttachedDocumentServiceImpl.class);

    private final AttachedDocumentRepository attachedDocumentRepository;

    public AttachedDocumentServiceImpl(AttachedDocumentRepository attachedDocumentRepository) {
        this.attachedDocumentRepository = attachedDocumentRepository;
    }

    @Override
    public AttachedDocument save(AttachedDocument attachedDocument) {
        log.debug("Request to save AttachedDocument : {}", attachedDocument);
        return attachedDocumentRepository.save(attachedDocument);
    }

    @Override
    public AttachedDocument update(AttachedDocument attachedDocument) {
        log.debug("Request to update AttachedDocument : {}", attachedDocument);
        // no save call needed as we have no fields that can be updated
        return attachedDocument;
    }

    @Override
    public Optional<AttachedDocument> partialUpdate(AttachedDocument attachedDocument) {
        log.debug("Request to partially update AttachedDocument : {}", attachedDocument);

        return attachedDocumentRepository
            .findById(attachedDocument.getId())
            .map(existingAttachedDocument -> {
                return existingAttachedDocument;
            })// .map(attachedDocumentRepository::save)
        ;
    }

    @Override
    public List<AttachedDocument> findAll() {
        log.debug("Request to get all AttachedDocuments");
        return attachedDocumentRepository.findAll();
    }

    @Override
    public Optional<AttachedDocument> findOne(String id) {
        log.debug("Request to get AttachedDocument : {}", id);
        return attachedDocumentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AttachedDocument : {}", id);
        attachedDocumentRepository.deleteById(id);
    }
}
