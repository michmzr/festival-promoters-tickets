package eu.cybershu.service.impl;

import com.google.zxing.WriterException;
import eu.cybershu.domain.PromoCode;
import eu.cybershu.domain.Promotor;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.*;
import eu.cybershu.service.QRCodeService;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.mapper.TicketMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final String QRCODE_FORMAT = "png";

    private final String ticketDomainUrl;

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final QRCodeService qrCodeService;

    private final GuestRepository guestRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PromotorRepository promotorRepository;
    private final PromoCodeRepository promoCodeRepository;


    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             TicketTypeRepository ticketTypeRepository, PromotorRepository promotorRepository,
                             QRCodeService qrCodeService, PromoCodeRepository promoCodeRepository,
                             @Value("${application.tickets.domain}") String ticketDomainUrl,

                             GuestRepository guestRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketTypeRepository = ticketTypeRepository;
        this.promotorRepository = promotorRepository;
        this.qrCodeService = qrCodeService;
        this.promoCodeRepository = promoCodeRepository;
        this.ticketDomainUrl = ticketDomainUrl;
        this.guestRepository = guestRepository;
    }

    public Optional<TicketDTO> findByGuestIdTicketTypeAndOrderId(Long guestId, Long ticketTypeId, String orderId) {
        log.debug("Looking for a ticket: guest: {}, ticket type: {}, order id: {}", guestId, ticketTypeId, orderId);

        Optional<Ticket> ticketOpt = ticketRepository
            .findByGuestIdAndTicketTypeIdAndOrderIdAndEnabledTrue(guestId, ticketTypeId, orderId);
        return ticketOpt.map(ticketMapper::toDto);
    }

    @Override
    public TicketDTO create(TicketCreateDTO ticketDTO) throws WriterException, IOException {
        log.debug("Request to save Ticket : {}", ticketDTO);

        Ticket ticket = ticketMapper.toEntity(ticketDTO);

        UUID uuid = UUID.randomUUID();

        String ticketUrl = ticketUrl(uuid);

        ticket.setUuid(uuid);
        ticket.setTicketQR(generateQrCode(ticketUrl));
        ticket.setTicketQRContentType(QRCODE_FORMAT);
        ticket.setTicketUrl(ticketUrl);
        ticket.setTicketPrice(ticketDTO.getTicketPrice());
        ticket.setOrderId(ticketDTO.getOrderId());
        //todo unikalny hash biletu w celu weryfikacji, czy bilet został już wygenerowany

        if (ticketDTO.getTicketTypeId() != null) {
            TicketType ticketType = ticketTypeRepository.getOne(ticketDTO.getTicketTypeId());
            log.info("Ticket type: {}", ticketType);
            if (ticketType != null) {
                ticket.setTicketType(ticketType);
            }
        }

        if (ticketDTO.getPromoCodeId() != null) {
            PromoCode promoCode = promoCodeRepository.getOne(ticketDTO.getPromoCodeId());
            log.info("Promo code: {}", promoCode);

            ticket.setPromoCode(promoCode);
        }

        if (ticketDTO.getPromotorId() != null) {
            Promotor promotor = promotorRepository.getOne(ticketDTO.getPromotorId());
            log.info("Promotor {}", promotor);

            ticket.setPromotor(promotor);
        }

        Long guestId = ticketDTO.getGuestId();
        ticket.setGuest(guestRepository.getOne(guestId));

        ticket.setCreatedAt(Instant.now());
        ticket.setEnabled(true);

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }

    private String ticketUrl(UUID uuid) {
        String ticketDomainUrl = this.ticketDomainUrl;
        if (!ticketDomainUrl.endsWith("/"))
            ticketDomainUrl = ticketDomainUrl.concat("/");

        return ticketDomainUrl + "ticket/verify/" + uuid;
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
     * Get all the tickets where Guest is {@code null}.
     *
     * @return the list of entities.
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
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findByUUID(String uuid) {
        log.debug("Request to get Ticket by uuid : {}", uuid);
        return ticketRepository.findByUuidEquals(UUID.fromString(uuid))
            .map(ticketMapper::toDto);
    }

    @Override
    @Transactional
    public void validateTicket(Long id) {
        log.debug("Validating ticket {}", id);

        Ticket ticket = ticketRepository.getOne(id);
        ticket.setValidatedAt(Instant.now());
        ticketRepository.saveAndFlush(ticket);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
    }
}
