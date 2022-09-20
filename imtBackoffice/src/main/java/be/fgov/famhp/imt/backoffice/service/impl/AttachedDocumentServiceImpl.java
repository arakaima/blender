package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.AttachedDocument;
import be.fgov.famhp.imt.backoffice.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.backoffice.service.AttachedDocumentService;
import be.fgov.famhp.imt.backoffice.service.dto.AttachedDocumentDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.AttachedDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final AttachedDocumentMapper attachedDocumentMapper;

    public AttachedDocumentServiceImpl(
        AttachedDocumentRepository attachedDocumentRepository,
        AttachedDocumentMapper attachedDocumentMapper
    ) {
        this.attachedDocumentRepository = attachedDocumentRepository;
        this.attachedDocumentMapper = attachedDocumentMapper;
    }

    @Override
    public AttachedDocumentDTO save(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to save AttachedDocument : {}", attachedDocumentDTO);
        AttachedDocument attachedDocument = attachedDocumentMapper.toEntity(attachedDocumentDTO);
        attachedDocument = attachedDocumentRepository.save(attachedDocument);
        return attachedDocumentMapper.toDto(attachedDocument);
    }

    @Override
    public AttachedDocumentDTO update(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to update AttachedDocument : {}", attachedDocumentDTO);
        AttachedDocument attachedDocument = attachedDocumentMapper.toEntity(attachedDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return attachedDocumentMapper.toDto(attachedDocument);
    }

    @Override
    public Optional<AttachedDocumentDTO> partialUpdate(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to partially update AttachedDocument : {}", attachedDocumentDTO);

        return attachedDocumentRepository
            .findById(attachedDocumentDTO.getId())
            .map(existingAttachedDocument -> {
                attachedDocumentMapper.partialUpdate(existingAttachedDocument, attachedDocumentDTO);

                return existingAttachedDocument;
            })
            // .map(attachedDocumentRepository::save)
            .map(attachedDocumentMapper::toDto);
    }

    @Override
    public List<AttachedDocumentDTO> findAll() {
        log.debug("Request to get all AttachedDocuments");
        return attachedDocumentRepository
            .findAll()
            .stream()
            .map(attachedDocumentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<AttachedDocumentDTO> findOne(String id) {
        log.debug("Request to get AttachedDocument : {}", id);
        return attachedDocumentRepository.findById(id).map(attachedDocumentMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete AttachedDocument : {}", id);
        attachedDocumentRepository.deleteById(id);
    }
}
