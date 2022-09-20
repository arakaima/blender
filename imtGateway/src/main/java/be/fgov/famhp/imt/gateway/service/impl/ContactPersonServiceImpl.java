package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.ContactPerson;
import be.fgov.famhp.imt.gateway.repository.ContactPersonRepository;
import be.fgov.famhp.imt.gateway.service.ContactPersonService;
import be.fgov.famhp.imt.gateway.service.dto.ContactPersonDTO;
import be.fgov.famhp.imt.gateway.service.mapper.ContactPersonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ContactPerson}.
 */
@Service
public class ContactPersonServiceImpl implements ContactPersonService {

    private final Logger log = LoggerFactory.getLogger(ContactPersonServiceImpl.class);

    private final ContactPersonRepository contactPersonRepository;

    private final ContactPersonMapper contactPersonMapper;

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository, ContactPersonMapper contactPersonMapper) {
        this.contactPersonRepository = contactPersonRepository;
        this.contactPersonMapper = contactPersonMapper;
    }

    @Override
    public Mono<ContactPersonDTO> save(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to save ContactPerson : {}", contactPersonDTO);
        return contactPersonRepository.save(contactPersonMapper.toEntity(contactPersonDTO)).map(contactPersonMapper::toDto);
    }

    @Override
    public Mono<ContactPersonDTO> update(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to update ContactPerson : {}", contactPersonDTO);
        // no save call needed as we have no fields that can be updated
        return contactPersonRepository.findById(contactPersonDTO.getId()).map(contactPersonMapper::toDto);
    }

    @Override
    public Mono<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to partially update ContactPerson : {}", contactPersonDTO);

        return contactPersonRepository
            .findById(contactPersonDTO.getId())
            .map(existingContactPerson -> {
                contactPersonMapper.partialUpdate(existingContactPerson, contactPersonDTO);

                return existingContactPerson;
            })
            // .flatMap(contactPersonRepository::save)
            .map(contactPersonMapper::toDto);
    }

    @Override
    public Flux<ContactPersonDTO> findAll() {
        log.debug("Request to get all ContactPeople");
        return contactPersonRepository.findAll().map(contactPersonMapper::toDto);
    }

    public Mono<Long> countAll() {
        return contactPersonRepository.count();
    }

    @Override
    public Mono<ContactPersonDTO> findOne(String id) {
        log.debug("Request to get ContactPerson : {}", id);
        return contactPersonRepository.findById(id).map(contactPersonMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete ContactPerson : {}", id);
        return contactPersonRepository.deleteById(id);
    }
}
