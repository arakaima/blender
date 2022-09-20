package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import be.fgov.famhp.imt.backoffice.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.CapaDocumentService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link CapaDocument}.
 */
@Service
public class CapaDocumentServiceImpl implements CapaDocumentService {

    private final Logger log = LoggerFactory.getLogger(CapaDocumentServiceImpl.class);

    private final CapaDocumentRepository capaDocumentRepository;

    public CapaDocumentServiceImpl(CapaDocumentRepository capaDocumentRepository) {
        this.capaDocumentRepository = capaDocumentRepository;
    }

    @Override
    public CapaDocument save(CapaDocument capaDocument) {
        log.debug("Request to save CapaDocument : {}", capaDocument);
        return capaDocumentRepository.save(capaDocument);
    }

    @Override
    public CapaDocument update(CapaDocument capaDocument) {
        log.debug("Request to update CapaDocument : {}", capaDocument);
        // no save call needed as we have no fields that can be updated
        return capaDocument;
    }

    @Override
    public Optional<CapaDocument> partialUpdate(CapaDocument capaDocument) {
        log.debug("Request to partially update CapaDocument : {}", capaDocument);

        return capaDocumentRepository
            .findById(capaDocument.getId())
            .map(existingCapaDocument -> {
                return existingCapaDocument;
            })// .map(capaDocumentRepository::save)
        ;
    }

    @Override
    public List<CapaDocument> findAll() {
        log.debug("Request to get all CapaDocuments");
        return capaDocumentRepository.findAll();
    }

    @Override
    public Optional<CapaDocument> findOne(String id) {
        log.debug("Request to get CapaDocument : {}", id);
        return capaDocumentRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CapaDocument : {}", id);
        capaDocumentRepository.deleteById(id);
    }
}
