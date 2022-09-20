package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.CapaDocument;
import be.fgov.famhp.imt.gateway.repository.CapaDocumentRepository;
import be.fgov.famhp.imt.gateway.service.CapaDocumentService;
import be.fgov.famhp.imt.gateway.service.dto.CapaDocumentDTO;
import be.fgov.famhp.imt.gateway.service.mapper.CapaDocumentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CapaDocumentDTO> save(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to save CapaDocument : {}", capaDocumentDTO);
        return capaDocumentRepository.save(capaDocumentMapper.toEntity(capaDocumentDTO)).map(capaDocumentMapper::toDto);
    }

    @Override
    public Mono<CapaDocumentDTO> update(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to update CapaDocument : {}", capaDocumentDTO);
        // no save call needed as we have no fields that can be updated
        return capaDocumentRepository.findById(capaDocumentDTO.getId()).map(capaDocumentMapper::toDto);
    }

    @Override
    public Mono<CapaDocumentDTO> partialUpdate(CapaDocumentDTO capaDocumentDTO) {
        log.debug("Request to partially update CapaDocument : {}", capaDocumentDTO);

        return capaDocumentRepository
            .findById(capaDocumentDTO.getId())
            .map(existingCapaDocument -> {
                capaDocumentMapper.partialUpdate(existingCapaDocument, capaDocumentDTO);

                return existingCapaDocument;
            })
            // .flatMap(capaDocumentRepository::save)
            .map(capaDocumentMapper::toDto);
    }

    @Override
    public Flux<CapaDocumentDTO> findAll() {
        log.debug("Request to get all CapaDocuments");
        return capaDocumentRepository.findAll().map(capaDocumentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return capaDocumentRepository.count();
    }

    @Override
    public Mono<CapaDocumentDTO> findOne(String id) {
        log.debug("Request to get CapaDocument : {}", id);
        return capaDocumentRepository.findById(id).map(capaDocumentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete CapaDocument : {}", id);
        return capaDocumentRepository.deleteById(id);
    }
}
