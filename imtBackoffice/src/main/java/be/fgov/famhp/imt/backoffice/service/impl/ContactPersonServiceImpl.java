package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import be.fgov.famhp.imt.backoffice.repository.ContactPersonRepository;
import be.fgov.famhp.imt.backoffice.service.ContactPersonService;
import be.fgov.famhp.imt.backoffice.service.dto.ContactPersonDTO;
import be.fgov.famhp.imt.backoffice.service.mapper.ContactPersonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public ContactPersonDTO save(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to save ContactPerson : {}", contactPersonDTO);
        ContactPerson contactPerson = contactPersonMapper.toEntity(contactPersonDTO);
        contactPerson = contactPersonRepository.save(contactPerson);
        return contactPersonMapper.toDto(contactPerson);
    }

    @Override
    public ContactPersonDTO update(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to update ContactPerson : {}", contactPersonDTO);
        ContactPerson contactPerson = contactPersonMapper.toEntity(contactPersonDTO);
        // no save call needed as we have no fields that can be updated
        return contactPersonMapper.toDto(contactPerson);
    }

    @Override
    public Optional<ContactPersonDTO> partialUpdate(ContactPersonDTO contactPersonDTO) {
        log.debug("Request to partially update ContactPerson : {}", contactPersonDTO);

        return contactPersonRepository
            .findById(contactPersonDTO.getId())
            .map(existingContactPerson -> {
                contactPersonMapper.partialUpdate(existingContactPerson, contactPersonDTO);

                return existingContactPerson;
            })
            // .map(contactPersonRepository::save)
            .map(contactPersonMapper::toDto);
    }

    @Override
    public List<ContactPersonDTO> findAll() {
        log.debug("Request to get all ContactPeople");
        return contactPersonRepository.findAll().stream().map(contactPersonMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ContactPersonDTO> findOne(String id) {
        log.debug("Request to get ContactPerson : {}", id);
        return contactPersonRepository.findById(id).map(contactPersonMapper::toDto);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ContactPerson : {}", id);
        contactPersonRepository.deleteById(id);
    }
}
