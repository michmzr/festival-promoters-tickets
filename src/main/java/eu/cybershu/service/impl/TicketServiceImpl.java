package eu.cybershu.service.impl;

import com.google.zxing.WriterException;
import eu.cybershu.domain.PromoCode;
import eu.cybershu.domain.Promotor;
import eu.cybershu.domain.Ticket;
import eu.cybershu.domain.TicketType;
import eu.cybershu.repository.PromoCodeRepository;
import eu.cybershu.repository.PromotorRepository;
import eu.cybershu.repository.TicketRepository;
import eu.cybershu.repository.TicketTypeRepository;
import eu.cybershu.service.QRCodeService;
import eu.cybershu.service.TicketService;
import eu.cybershu.service.dto.TicketCreateDTO;
import eu.cybershu.service.dto.TicketDTO;
import eu.cybershu.service.mapper.TicketMapper;
import lombok.extern.slf4j.Slf4j;
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

    public static final String QRCODE_FORMAT = "png";

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final TicketTypeRepository ticketTypeRepository;
    private final PromotorRepository promotorRepository;
    private final QRCodeService qrCodeService;
    private final PromoCodeRepository promoCodeRepository;


    public TicketServiceImpl(TicketRepository ticketRepository, TicketMapper ticketMapper,
                             TicketTypeRepository ticketTypeRepository, PromotorRepository promotorRepository,
                             QRCodeService qrCodeService, PromoCodeRepository promoCodeRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketTypeRepository = ticketTypeRepository;
        this.promotorRepository = promotorRepository;
        this.qrCodeService = qrCodeService;
        this.promoCodeRepository = promoCodeRepository;
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

        if(ticketDTO.getTicketTypeId() != null) {
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

        if (ticketDTO.getPromotorId() != null)  {
            Promotor promotor = promotorRepository.getOne(ticketDTO.getPromotorId());
            log.info("Promotor {}", promotor);

            ticket.setPromotor(promotor);
        }

        ticket.setCreatedAt(Instant.now());
        ticket.setEnabled(true);

        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
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
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
    }
}
