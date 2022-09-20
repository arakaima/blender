package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Requests}.
 */
@Service
public class RequestsServiceImpl implements RequestsService {

    private final Logger log = LoggerFactory.getLogger(RequestsServiceImpl.class);

    private final RequestsRepository requestsRepository;

    public RequestsServiceImpl(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    @Override
    public Requests save(Requests requests) {
        log.debug("Request to save Requests : {}", requests);
        return requestsRepository.save(requests);
    }

    @Override
    public Requests update(Requests requests) {
        log.debug("Request to update Requests : {}", requests);
        return requestsRepository.save(requests);
    }

    @Override
    public Optional<Requests> partialUpdate(Requests requests) {
        log.debug("Request to partially update Requests : {}", requests);

        return requestsRepository
            .findById(requests.getId())
            .map(existingRequests -> {
                if (requests.getInspectionId() != null) {
                    existingRequests.setInspectionId(requests.getInspectionId());
                }
                if (requests.getTitle() != null) {
                    existingRequests.setTitle(requests.getTitle());
                }
                if (requests.getDatetime() != null) {
                    existingRequests.setDatetime(requests.getDatetime());
                }
                if (requests.getStatus() != null) {
                    existingRequests.setStatus(requests.getStatus());
                }

                return existingRequests;
            })
            .map(requestsRepository::save);
    }

    @Override
    public List<Requests> findAll() {
        log.debug("Request to get all Requests");
        return requestsRepository.findAll();
    }

    @Override
    public Optional<Requests> findOne(String id) {
        log.debug("Request to get Requests : {}", id);
        return requestsRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Requests : {}", id);
        requestsRepository.deleteById(id);
    }
}
