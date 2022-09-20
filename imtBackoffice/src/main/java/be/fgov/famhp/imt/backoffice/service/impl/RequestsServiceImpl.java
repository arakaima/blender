package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.Requests;
import be.fgov.famhp.imt.backoffice.repository.RequestsRepository;
import be.fgov.famhp.imt.backoffice.service.RequestsService;
import be.fgov.famhp.imt.backoffice.service.dto.RequestsDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.RequestsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final RequestsMapper requestsMapper;

    public RequestsServiceImpl(RequestsRepository requestsRepository, RequestsMapper requestsMapper) {
        this.requestsRepository = requestsRepository;
        this.requestsMapper = requestsMapper;
    }

    @Override
    public RequestsDTO save(RequestsDTO requestsDTO) {
        log.debug("Request to save Requests : {}", requestsDTO);
        Requests requests = requestsMapper.toEntity(requestsDTO);
        requests = requestsRepository.save(requests);
        return requestsMapper.toDto(requests);
    }

    @Override
    public RequestsDTO update(RequestsDTO requestsDTO) {
        log.debug("Request to update Requests : {}", requestsDTO);
        Requests requests = requestsMapper.toEntity(requestsDTO);
        // no save call needed as we have no fields that can be updated
        return requestsMapper.toDto(requests);
    }

    @Override
    public Optional<RequestsDTO> partialUpdate(RequestsDTO requestsDTO) {
        log.debug("Request to partially update Requests : {}", requestsDTO);

        return requestsRepository
            .findById(requestsDTO.getId())
            .map(existingRequests -> {
                requestsMapper.partialUpdate(existingRequests, requestsDTO);

                return existingRequests;
            })
            // .map(requestsRepository::save)
            .map(requestsMapper::toDto);
    }

    @Override
    public List<RequestsDTO> findAll() {
        log.debug("Request to get all Requests");
        return requestsRepository.findAll().stream().map(requestsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<RequestsDTO> findOne(String id) {
        log.debug("Request to get Requests : {}", id);
        return requestsRepository.findById(id).map(requestsMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Requests : {}", id);
        requestsRepository.deleteById(id);
    }
}
