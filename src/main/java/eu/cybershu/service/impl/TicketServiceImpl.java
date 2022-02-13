package eu.cybershu.service.impl;

import com.google.zxing.WriterException;
import eu.cybershu.service.QRCodeService;
import eu.cybershu.service.TicketService;
import eu.cybershu.domain.Ticket;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.mapper.TicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Ticket}.
 */
@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    public static final String QRCODE_FORMAT = "png";
    private final Logger log = LoggerFactory.getLogger(TicketServiceImpl.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;
    private final QRCodeService qrCodeService;

    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper, QRCodeService qrCodeService) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.qrCodeService = qrCodeService;
    }

    @Override
    public TicketDTO create(TicketDTO ticketDTO) throws WriterException, IOException {
        log.debug("Request to save Ticket : {}", ticketDTO);
        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        UUID uuid = UUID.randomUUID();

        String ticketUrl = ticketUrl(uuid);
        ticket.setUuid(uuid);
        ticket.setTicketQR(generateQrCode(ticketUrl));
        ticket.setTicketQRContentType(QRCODE_FORMAT);
        ticket.setTicketUrl(ticketUrl);
        ticket.setCreatedAt(Instant.now());
        ticket.setEnabled(true);

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }

    @Override
    public TicketDTO create(Long ticketTypeId) throws IOException, WriterException {
        log.info("Request to simple save ticket: {}", ticketTypeId);

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketTypeId(ticketTypeId);

        return create(ticketDTO);
    }

    private String ticketUrl(UUID uuid) {
        return "/api/ticket/verify/" + uuid;
    }

    private byte[] generateQrCode(String qrCodeText) throws WriterException, IOException {
        BufferedImage bufferedImage = qrCodeService.generateQRCode(qrCodeText);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, QRCODE_FORMAT, baos);
        return baos.toByteArray();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> findAll() {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAll().stream()
            .map(ticketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the tickets where Guest is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> findAllWhereGuestIsNull() {
        log.debug("Request to get all tickets where Guest is null");
        return StreamSupport
            .stream(ticketRepository.findAll().spliterator(), false)
            .filter(ticket -> ticket.getGuest() == null)
            .map(ticketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findById(id)
            .map(ticketMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
    }
}
