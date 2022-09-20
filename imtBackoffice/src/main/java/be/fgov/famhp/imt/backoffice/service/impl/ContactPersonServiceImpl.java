package be.fgov.famhp.imt.backoffice.service.impl;

import be.fgov.famhp.imt.backoffice.domain.ContactPerson;
import be.fgov.famhp.imt.backoffice.repository.ContactPersonRepository;
import be.fgov.famhp.imt.backoffice.service.ContactPersonService;
import java.util.List;
import java.util.Optional;
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

    public ContactPersonServiceImpl(ContactPersonRepository contactPersonRepository) {
        this.contactPersonRepository = contactPersonRepository;
    }

    @Override
    public ContactPerson save(ContactPerson contactPerson) {
        log.debug("Request to save ContactPerson : {}", contactPerson);
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public ContactPerson update(ContactPerson contactPerson) {
        log.debug("Request to update ContactPerson : {}", contactPerson);
        return contactPersonRepository.save(contactPerson);
    }

    @Override
    public Optional<ContactPerson> partialUpdate(ContactPerson contactPerson) {
        log.debug("Request to partially update ContactPerson : {}", contactPerson);

        return contactPersonRepository
            .findById(contactPerson.getId())
            .map(existingContactPerson -> {
                if (contactPerson.getNiss() != null) {
                    existingContactPerson.setNiss(contactPerson.getNiss());
                }
                if (contactPerson.getFirstName() != null) {
                    existingContactPerson.setFirstName(contactPerson.getFirstName());
                }
                if (contactPerson.getLastName() != null) {
                    existingContactPerson.setLastName(contactPerson.getLastName());
                }
                if (contactPerson.getLanguage() != null) {
                    existingContactPerson.setLanguage(contactPerson.getLanguage());
                }
                if (contactPerson.getEmail() != null) {
                    existingContactPerson.setEmail(contactPerson.getEmail());
                }
                if (contactPerson.getPhoneNumber() != null) {
                    existingContactPerson.setPhoneNumber(contactPerson.getPhoneNumber());
                }
                if (contactPerson.getRole() != null) {
                    existingContactPerson.setRole(contactPerson.getRole());
                }

                return existingContactPerson;
            })
            .map(contactPersonRepository::save);
    }

    @Override
    public List<ContactPerson> findAll() {
        log.debug("Request to get all ContactPeople");
        return contactPersonRepository.findAll();
    }

    @Override
    public Optional<ContactPerson> findOne(String id) {
        log.debug("Request to get ContactPerson : {}", id);
        return contactPersonRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete ContactPerson : {}", id);
        contactPersonRepository.deleteById(id);
    }
}
