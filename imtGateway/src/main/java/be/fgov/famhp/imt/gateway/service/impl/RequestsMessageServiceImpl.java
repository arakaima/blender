package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.RequestsMessage;
import be.fgov.famhp.imt.gateway.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.gateway.service.RequestsMessageService;
import be.fgov.famhp.imt.gateway.service.dto.RequestsMessageDTO;
import be.fgov.famhp.imt.gateway.service.mapper.RequestsMessageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link RequestsMessage}.
 */
@Service
public class RequestsMessageServiceImpl implements RequestsMessageService {

    private final Logger log = LoggerFactory.getLogger(RequestsMessageServiceImpl.class);

    private final RequestsMessageRepository requestsMessageRepository;

    private final RequestsMessageMapper requestsMessageMapper;

    public RequestsMessageServiceImpl(RequestsMessageRepository requestsMessageRepository, RequestsMessageMapper requestsMessageMapper) {
        this.requestsMessageRepository = requestsMessageRepository;
        this.requestsMessageMapper = requestsMessageMapper;
    }

    @Override
    public Mono<RequestsMessageDTO> save(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to save RequestsMessage : {}", requestsMessageDTO);
        return requestsMessageRepository.save(requestsMessageMapper.toEntity(requestsMessageDTO)).map(requestsMessageMapper::toDto);
    }

    @Override
    public Mono<RequestsMessageDTO> update(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to update RequestsMessage : {}", requestsMessageDTO);
        // no save call needed as we have no fields that can be updated
        return requestsMessageRepository.findById(requestsMessageDTO.getId()).map(requestsMessageMapper::toDto);
    }

    @Override
    public Mono<RequestsMessageDTO> partialUpdate(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to partially update RequestsMessage : {}", requestsMessageDTO);

        return requestsMessageRepository
            .findById(requestsMessageDTO.getId())
            .map(existingRequestsMessage -> {
                requestsMessageMapper.partialUpdate(existingRequestsMessage, requestsMessageDTO);

                return existingRequestsMessage;
            })
            // .flatMap(requestsMessageRepository::save)
            .map(requestsMessageMapper::toDto);
    }

    @Override
    public Flux<RequestsMessageDTO> findAll() {
        log.debug("Request to get all RequestsMessages");
        return requestsMessageRepository.findAll().map(requestsMessageMapper::toDto);
    }

    public Mono<Long> countAll() {
        return requestsMessageRepository.count();
    }

    @Override
    public Mono<RequestsMessageDTO> findOne(String id) {
        log.debug("Request to get RequestsMessage : {}", id);
        return requestsMessageRepository.findById(id).map(requestsMessageMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete RequestsMessage : {}", id);
        return requestsMessageRepository.deleteById(id);
    }
}
