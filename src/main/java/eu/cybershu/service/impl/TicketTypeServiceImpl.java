package eu.cybershu.service.impl;

import eu.cybershu.service.TicketTypeService;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.dto.TicketTypeDTO;
import eu.cybershu.service.mapper.TicketTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link TicketType}.
 */
@Service
@Transactional
public class TicketTypeServiceImpl implements TicketTypeService {

    private final Logger log = LoggerFactory.getLogger(TicketTypeServiceImpl.class);

    private final TicketTypeRepository ticketTypeRepository;

    private final TicketTypeMapper ticketTypeMapper;

    public TicketTypeServiceImpl(TicketTypeRepository ticketTypeRepository, TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.ticketTypeMapper = ticketTypeMapper;
    }

    @Override
    public TicketTypeDTO save(TicketTypeDTO ticketTypeDTO) {
        log.debug("Request to save TicketType : {}", ticketTypeDTO);
        TicketType ticketType = ticketTypeMapper.toEntity(ticketTypeDTO);
        ticketType = ticketTypeRepository.save(ticketType);
        return ticketTypeMapper.toDto(ticketType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketTypeDTO> findAll() {
        log.debug("Request to get all TicketTypes");
        return ticketTypeRepository.findAll().stream()
            .map(ticketTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
     *  Get all the ticketTypes where Ticket is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<TicketTypeDTO> findAllWhereTicketIsNull() {
        log.debug("Request to get all ticketTypes where Ticket is null");
        return StreamSupport
            .stream(ticketTypeRepository.findAll().spliterator(), false)
            .filter(ticketType -> ticketType.getTicket() == null)
            .map(ticketTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketTypeDTO> findOne(Long id) {
        log.debug("Request to get TicketType : {}", id);
        return ticketTypeRepository.findById(id)
            .map(ticketTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketType : {}", id);
        ticketTypeRepository.deleteById(id);
    }
}
