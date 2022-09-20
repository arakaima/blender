package be.fgov.famhp.imt.gateway.service.impl;

import be.fgov.famhp.imt.gateway.domain.Message;
import be.fgov.famhp.imt.gateway.repository.MessageRepository;
import be.fgov.famhp.imt.gateway.service.MessageService;
import be.fgov.famhp.imt.gateway.service.dto.MessageDTO;
import be.fgov.famhp.imt.gateway.service.mapper.MessageMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Message}.
 */
@Service
public class MessageServiceImpl implements MessageService {

    private final Logger log = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;

    private final MessageMapper messageMapper;

    public MessageServiceImpl(MessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public Mono<MessageDTO> save(MessageDTO messageDTO) {
        log.debug("Request to save Message : {}", messageDTO);
        return messageRepository.save(messageMapper.toEntity(messageDTO)).map(messageMapper::toDto);
    }

    @Override
    public Mono<MessageDTO> update(MessageDTO messageDTO) {
        log.debug("Request to update Message : {}", messageDTO);
        // no save call needed as we have no fields that can be updated
        return messageRepository.findById(messageDTO.getId()).map(messageMapper::toDto);
    }

    @Override
    public Mono<MessageDTO> partialUpdate(MessageDTO messageDTO) {
        log.debug("Request to partially update Message : {}", messageDTO);

        return messageRepository
            .findById(messageDTO.getId())
            .map(existingMessage -> {
                messageMapper.partialUpdate(existingMessage, messageDTO);

                return existingMessage;
            })
            // .flatMap(messageRepository::save)
            .map(messageMapper::toDto);
    }

    @Override
    public Flux<MessageDTO> findAll() {
        log.debug("Request to get all Messages");
        return messageRepository.findAll().map(messageMapper::toDto);
    }

    public Mono<Long> countAll() {
        return messageRepository.count();
    }

    @Override
    public Mono<MessageDTO> findOne(String id) {
        log.debug("Request to get Message : {}", id);
        return messageRepository.findById(id).map(messageMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Message : {}", id);
        return messageRepository.deleteById(id);
    }
}
