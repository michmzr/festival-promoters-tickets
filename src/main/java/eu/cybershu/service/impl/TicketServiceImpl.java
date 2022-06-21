package eu.cybershu.service.impl;

import com.google.zxing.WriterException;
import com.lowagie.text.DocumentException;
import eu.cybershu.domain.Guest;
import eu.cybershu.domain.PromoCode;
import eu.cybershu.domain.Promotor;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.GuestRepository;
import eu.cybershu.repository.PromoCodeRepository;
import eu.cybershu.repository.PromotorRepository;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.GuestService;
import eu.cybershu.service.QRCodeService;
import eu.cybershu.service.TicketPDFFileService;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.dto.TicketListingItemDTO;
import eu.cybershu.service.dto.TicketPDFData;
import eu.cybershu.service.mapper.TicketListingMapper;
import eu.cybershu.service.mapper.TicketMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ticket}.
 */
@Slf4j
@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private static final String QRCODE_FORMAT = "jpg";
    private static final String TICKET_FILE_CONTENT_TYPE = "application/pdf";

    private final String ticketDomainUrl;

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;
    private final TicketListingMapper ticketListingMapper;

    private final QRCodeService qrCodeService;

    private final GuestRepository guestRepository;
    private final GuestService guestService;
    private final TicketTypeRepository ticketTypeRepository;
    private final PromotorRepository promotorRepository;
    private final PromoCodeRepository promoCodeRepository;

    private final TicketPDFFileService ticketPDFFileService;

    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
        TicketListingMapper ticketListingMapper, TicketTypeRepository ticketTypeRepository,
        PromotorRepository promotorRepository,
        QRCodeService qrCodeService, PromoCodeRepository promoCodeRepository,
        @Value("${application.tickets.domain}") String ticketDomainUrl,

        GuestRepository guestRepository, GuestService guestService,
        TicketPDFFileService ticketPDFFileService) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketListingMapper = ticketListingMapper;
        this.ticketTypeRepository = ticketTypeRepository;
        this.promotorRepository = promotorRepository;
        this.qrCodeService = qrCodeService;
        this.promoCodeRepository = promoCodeRepository;
        this.ticketDomainUrl = ticketDomainUrl;
        this.guestRepository = guestRepository;
        this.guestService = guestService;
        this.ticketPDFFileService = ticketPDFFileService;
    }

    public Optional<TicketDTO> findByGuestIdTicketTypeAndOrderId(Long guestId, Long ticketTypeId, String orderId) {
        log.debug("Looking for a ticket: guest: {}, ticket type: {}, order id: {}", guestId, ticketTypeId, orderId);

        Optional<Ticket> ticketOpt = ticketRepository
            .findByGuestIdAndTicketTypeIdAndOrderIdAndEnabledTrue(guestId, ticketTypeId, orderId);
        return ticketOpt.map(ticketMapper::toDto);
    }

    @Override
    public TicketDTO create(TicketCreateDTO ticketCreateDTO) throws WriterException, IOException, DocumentException {
        log.debug("Request to save Ticket : {}", ticketCreateDTO);

        Ticket ticket = ticketMapper.toEntity(ticketCreateDTO);

        //ticket type
        TicketType ticketType = ticketTypeRepository.getOne(ticketCreateDTO.getTicketTypeId());
        log.debug("Ticket type: {}", ticketType);
        ticket.setTicketType(ticketType);

        //promotor or artist
        Promotor promotor = null;
        if (ticketCreateDTO.getPromotorId() != null) {
            promotor = promotorRepository.getOne(ticketCreateDTO.getPromotorId());
            log.debug("Promotor {}", promotor);
            ticket.setPromotor(promotor);
        }
        ticket.setArtistName(ticketCreateDTO.getArtistName());

        Long guestId = ticketCreateDTO.getGuestId();
        Guest guest = guestRepository.getOne(guestId);
        ticket.setGuest(guest);

        //other fields
        UUID uuid = UUID.randomUUID();
        String ticketUrl = ticketUrl(uuid);
        byte[] ticketQR = generateQrCode(ticketUrl);

        ticket.setUuid(uuid);
        ticket.setTicketQR(ticketQR);
        ticket.setTicketQRContentType(QRCODE_FORMAT);
        ticket.setTicketFileContentType(TICKET_FILE_CONTENT_TYPE);
        ticket.setTicketFile(generateTicketPdf(uuid, ticketQR, ticketType, guest, ticketCreateDTO.getArtistName(), promotor));
        ticket.setTicketUrl(ticketUrl);
        ticket.setTicketPrice(ticketCreateDTO.getTicketPrice());
        ticket.setTicketDiscount(ticketCreateDTO.getTicketDiscount());
        ticket.setOrderId(ticketCreateDTO.getOrderId());

        if (ticketCreateDTO.getPromoCodeId() != null) {
            PromoCode promoCode = promoCodeRepository.getOne(ticketCreateDTO.getPromoCodeId());
            log.info("Promo code: {}", promoCode);

            ticket.setPromoCode(promoCode);
        }


        ticket.setCreatedAt(Instant.now());
        ticket.setEnabled(true);

        ticket = ticketRepository.save(ticket);

        return ticketMapper.toDto(ticket);
    }

    private byte[] generateTicketPdf(UUID uuid,
                                     byte[] ticketQR,
                                     TicketType ticketType,
                                     Guest guest,
                                     String artistName,
                                     Promotor promotor) throws DocumentException, IOException {
        log.info("Generating pdf ticket: uuid={}, ticketType={}, guest={}, promotor={}", uuid, ticketType, guest, promotor);

        String format = QRCODE_FORMAT.contains("image") ? QRCODE_FORMAT : "image/" + QRCODE_FORMAT;
        TicketPDFData pdfData = TicketPDFData.builder()
            .uuid(uuid.toString())
            .qrFile(ticketQR)
            .qrFileContentType(format)
            .ticketType(ticketType)
            .guest(guest)
            .artistName(artistName)
            .promotor(promotor)
            .build();

        return ticketPDFFileService.generateTicketPDFFile(pdfData);
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
    public Optional<TicketDTO> regenerateTicketPdf(Long id) throws DocumentException, IOException {
        log.info("Regenerating ticket pdf {}", id);
        var ticketOpt = ticketRepository.findById(id);

        if (ticketOpt.isPresent()) {
            Ticket ticket = ticketOpt.get();

            ticket.setTicketFile(
                generateTicketPdf(ticket.getUuid(), ticket.getTicketQR(),
                    ticket.getTicketType(), ticket.getGuest(),
                    ticket.getArtistName(),
                    ticket.getPromotor()));
            ticketRepository.save(ticket);

            return Optional.of(ticketMapper.toDto(ticket));
        } else {
            log.warn("Ticket not found #{}", id);
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketDTO> findAll() {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAll().stream()
            .map(ticketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TicketListingItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Promotors");
        return ticketRepository
            .findAll(pageable)
            .map(ticketListingMapper::toDto);
    }

    /**
     * Get all the tickets where Guest is {@code null}.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> findAllWhereGuestIsNull() {
        log.debug("Request to get all tickets where Guest is null");
        return ticketRepository
            .findAll()
            .stream()
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

    @Override
    public void disable(Long id) {
        log.debug("Disabling ticket: {}", id);

        Ticket ticket = ticketRepository.getOne(id);
        ticket.enabled(false);
        ticket.setDisabledAt(Instant.now());
        ticketRepository.saveAndFlush(ticket);
    }

    @Override
    public void enable(Long id) {
        log.debug("Enabling ticket: {}", id);

        Ticket ticket = ticketRepository.getOne(id);
        ticket.setDisabledAt(null);
        ticket.enabled(true);
        ticketRepository.saveAndFlush(ticket);
    }
}
