package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.CapaDocument;
import be.fgov.famhp.imt.backoffice.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.CapaDocumentService;
import be.fgov.famhp.imt.backoffice.service.dto.CapaDocumentDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.CapaDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final CapaDocumentMapper capaDocumentMapper;

    public CapaDocumentServiceImpl(CapaDocumentRepository capaDocumentRepository, CapaDocumentMapper capaDocumentMapper) {
        this.capaDocumentRepository = capaDocumentRepository;
        this.capaDocumentMapper = capaDocumentMapper;
    }

    @Override
    public CapaDocumentDTO save(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to save CapaDocument : {}", capaDocumentDTO);
        CapaDocument capaDocument = capaDocumentMapper.toEntity(capaDocumentDTO);
        capaDocument = capaDocumentRepository.save(capaDocument);
        return capaDocumentMapper.toDto(capaDocument);
    }

    @Override
    public CapaDocumentDTO update(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to update CapaDocument : {}", capaDocumentDTO);
        CapaDocument capaDocument = capaDocumentMapper.toEntity(capaDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return capaDocumentMapper.toDto(capaDocument);
    }

    @Override
    public Optional<CapaDocumentDTO> partialUpdate(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to partially update CapaDocument : {}", capaDocumentDTO);

        return capaDocumentRepository
            .findById(capaDocumentDTO.getId())
            .map(existingCapaDocument -> {
                capaDocumentMapper.partialUpdate(existingCapaDocument, capaDocumentDTO);

                return existingCapaDocument;
            })
            // .map(capaDocumentRepository::save)
            .map(capaDocumentMapper::toDto);
    }

    @Override
    public List<CapaDocumentDTO> findAll() {
        log.debug("Request to get all CapaDocuments");
        return capaDocumentRepository.findAll().stream().map(capaDocumentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<CapaDocumentDTO> findOne(String id) {
        log.debug("Request to get CapaDocument : {}", id);
        return capaDocumentRepository.findById(id).map(capaDocumentMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete CapaDocument : {}", id);
        capaDocumentRepository.deleteById(id);
    }
}
