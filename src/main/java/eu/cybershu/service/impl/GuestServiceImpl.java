package eu.cybershu.service.impl;

import com.google.zxing.WriterException;
import eu.cybershu.domain.Guest;
import eu.cybershu.repository.GuestRepository;
import eu.cybershu.service.GuestService;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.GuestCreateDTO;
import eu.cybershu.service.dto.GuestDTO;
import eu.cybershu.service.dto.GuestUpdateDTO;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.mapper.GuestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Guest}.
 */
@Service
@Transactional
public class GuestServiceImpl implements GuestService {

    private final Logger log = LoggerFactory.getLogger(GuestServiceImpl.class);

    private final TicketService ticketService;

    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestServiceImpl(TicketService ticketService, GuestRepository guestRepository, GuestMapper guestMapper) {
        this.ticketService = ticketService;
        this.guestRepository = guestRepository;
        this.guestMapper = guestMapper;
    }

    @Override
    public GuestDTO save(GuestCreateDTO guestCreateDTO) throws IOException, WriterException {
        log.debug("Request to save Guest : {}", guestCreateDTO);

        TicketDTO ticket = ticketService.create(guestCreateDTO.getTicketTypeId());

        GuestDTO guestDto = guestMapper.toDto(guestCreateDTO);
        guestDto.setEnabled(true);
        guestDto.setCreatedAt(Instant.now());
        guestDto.setTicketId(ticket.getId());

        Guest guest = guestMapper.toEntity(guestDto);
        guest = guestRepository.save(guest);
        return guestMapper.toDto(guest);
    }

    @Override
    public GuestDTO save(GuestUpdateDTO guestUpdateDTO) {
        log.debug("Request to save updated Guest : {}", guestUpdateDTO);

        Guest guest = guestRepository.getOne(guestUpdateDTO.getId());
        log.debug("Guest: {}",  guest);

        GuestDTO guestDTO = guestMapper.update(
            guestMapper.toDto(guest),
            guestUpdateDTO
        );
        log.debug("GuestDTO: {}", guestDTO);

        guest = guestMapper.toEntity(guestDTO);
        guest = guestRepository.save(guest);
        return guestMapper.toDto(guest);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GuestDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Guests");
        return guestRepository.findAll(pageable)
            .map(guestMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<GuestDTO> findOne(Long id) {
        log.debug("Request to get Guest : {}", id);
        return guestRepository.findById(id)
            .map(guestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Guest : {}", id);
        guestRepository.deleteById(id);
    }
}
