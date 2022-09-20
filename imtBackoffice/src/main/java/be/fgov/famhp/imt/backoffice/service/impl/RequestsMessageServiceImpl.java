package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.RequestsMessage;
import be.fgov.famhp.imt.backoffice.repository.RequestsMessageRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsMessageService;
import java.util.List;
import java.util.Optional;
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

    public RequestsMessageServiceImpl(RequestsMessageRepository requestsMessageRepository) {
        this.requestsMessageRepository = requestsMessageRepository;
    }

    @Override
    public RequestsMessage save(RequestsMessage requestsMessage) {
        log.debug("Request to save RequestsMessage : {}", requestsMessage);
        return requestsMessageRepository.save(requestsMessage);
    }

    @Override
    public RequestsMessage update(RequestsMessage requestsMessage) {
        log.debug("Request to update RequestsMessage : {}", requestsMessage);
        return requestsMessageRepository.save(requestsMessage);
    }

    @Override
    public Optional<RequestsMessage> partialUpdate(RequestsMessage requestsMessage) {
        log.debug("Request to partially update RequestsMessage : {}", requestsMessage);

        return requestsMessageRepository
            .findById(requestsMessage.getId())
            .map(existingRequestsMessage -> {
                if (requestsMessage.getRequestId() != null) {
                    existingRequestsMessage.setRequestId(requestsMessage.getRequestId());
                }
                if (requestsMessage.getDatetime() != null) {
                    existingRequestsMessage.setDatetime(requestsMessage.getDatetime());
                }
                if (requestsMessage.getMessage() != null) {
                    existingRequestsMessage.setMessage(requestsMessage.getMessage());
                }
                if (requestsMessage.getAuthor() != null) {
                    existingRequestsMessage.setAuthor(requestsMessage.getAuthor());
                }

                return existingRequestsMessage;
            })
            .map(requestsMessageRepository::save);
    }

    @Override
    public List<RequestsMessage> findAll() {
        log.debug("Request to get all RequestsMessages");
        return requestsMessageRepository.findAll();
    }

    @Override
    public Optional<RequestsMessage> findOne(String id) {
        log.debug("Request to get RequestsMessage : {}", id);
        return requestsMessageRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete RequestsMessage : {}", id);
        requestsMessageRepository.deleteById(id);
    }
}
