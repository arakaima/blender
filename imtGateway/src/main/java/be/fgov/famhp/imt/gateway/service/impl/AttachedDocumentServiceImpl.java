package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.AttachedDocument;
import be.fgov.famhp.imt.gateway.repository.AttachedDocumentRepository;
import be.fgov.famhp.imt.gateway.service.AttachedDocumentService;
import be.fgov.famhp.imt.gateway.service.dto.AttachedDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.AttachedDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<AttachedDocumentDTO> save(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to save AttachedDocument : {}", attachedDocumentDTO);
        return attachedDocumentRepository.save(attachedDocumentMapper.toEntity(attachedDocumentDTO)).map(attachedDocumentMapper::toDto);
    }

    @Override
    public Mono<AttachedDocumentDTO> update(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to update AttachedDocument : {}", attachedDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return attachedDocumentRepository.findById(attachedDocumentDTO.getId()).map(attachedDocumentMapper::toDto);
    }

    @Override
    public Mono<AttachedDocumentDTO> partialUpdate(AttachedDocumentDTO attachedDocumentDTO) {
        log.debug("Request to partially update AttachedDocument : {}", attachedDocumentDTO);

        return attachedDocumentRepository
            .findById(attachedDocumentDTO.getId())
            .map(existingAttachedDocument -> {
                attachedDocumentMapper.partialUpdate(existingAttachedDocument, attachedDocumentDTO);

                return existingAttachedDocument;
            })
            // .flatMap(attachedDocumentRepository::save)
            .map(attachedDocumentMapper::toDto);
    }

    @Override
    public Flux<AttachedDocumentDTO> findAll() {
        log.debug("Request to get all AttachedDocuments");
        return attachedDocumentRepository.findAll().map(attachedDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return attachedDocumentRepository.count();
    }

    @Override
    public Mono<AttachedDocumentDTO> findOne(String id) {
        log.debug("Request to get AttachedDocument : {}", id);
        return attachedDocumentRepository.findById(id).map(attachedDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete AttachedDocument : {}", id);
        return attachedDocumentRepository.deleteById(id);
    }
}
