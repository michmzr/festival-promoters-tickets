package eu.cybershu.service.impl;

import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.TicketTypeService;
import eu.cybershu.service.dto.TicketTypeDTO;
import eu.cybershu.service.mapper.TicketTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TicketType}.
 */
@Slf4j
@Service
@Transactional
public class TicketTypeServiceImpl implements TicketTypeService {
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

        //todo unikalny productID
        //todo unikalny productUrl

        ticketType = ticketTypeRepository.save(ticketType);
        log.info("Saved ticket type: {}", ticketType);

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

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketTypeDTO> findOne(Long id) {
        log.debug("Request to get TicketType : {}", id);
        return ticketTypeRepository.findById(id)
            .map(ticketTypeMapper::toDto);
    }

    @Override
    public Optional<TicketTypeDTO> findOneByProductId(String productId) {
        log.debug("Request to get TicketType by product id {}", productId);

        return ticketTypeRepository.findByProductId(productId)
            .map(ticketTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketType : {}", id);
        ticketTypeRepository.deleteById(id);
    }
}
