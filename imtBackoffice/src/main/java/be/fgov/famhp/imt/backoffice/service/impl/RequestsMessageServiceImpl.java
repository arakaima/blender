package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import be.fgov.famhp.imt.backoffice.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsMessageService;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsMessageDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.RequestsMessageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public RequestsMessageDTO save(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to save RequestsMessage : {}", requestsMessageDTO);
        RequestsMessage requestsMessage = requestsMessageMapper.toEntity(requestsMessageDTO);
        requestsMessage = requestsMessageRepository.save(requestsMessage);
        return requestsMessageMapper.toDto(requestsMessage);
    }

    @Override
    public RequestsMessageDTO update(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to update RequestsMessage : {}", requestsMessageDTO);
        RequestsMessage requestsMessage = requestsMessageMapper.toEntity(requestsMessageDTO);
        // no save call needed as we have no fields that can be updated
        return requestsMessageMapper.toDto(requestsMessage);
    }

    @Override
    public Optional<RequestsMessageDTO> partialUpdate(RequestsMessageDTO requestsMessageDTO) {
        log.debug("Request to partially update RequestsMessage : {}", requestsMessageDTO);

        return requestsMessageRepository
            .findById(requestsMessageDTO.getId())
            .map(existingRequestsMessage -> {
                requestsMessageMapper.partialUpdate(existingRequestsMessage, requestsMessageDTO);

                return existingRequestsMessage;
            })
            // .map(requestsMessageRepository::save)
            .map(requestsMessageMapper::toDto);
    }

    @Override
    public List<RequestsMessageDTO> findAll() {
        log.debug("Request to get all RequestsMessages");
        return requestsMessageRepository
            .findAll()
            .stream()
            .map(requestsMessageMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<RequestsMessageDTO> findOne(String id) {
        log.debug("Request to get RequestsMessage : {}", id);
        return requestsMessageRepository.findById(id).map(requestsMessageMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RequestsMessage : {}", id);
        requestsMessageRepository.deleteById(id);
    }
}
