package eu.cybershu.service;

import eu.cybershu.service.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Slf4j
@Service
public class TicketVerificationService {
    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

    private final GuestService guestService;
    private final TicketService ticketService;
    private final TicketTypeService ticketTypeService;

    public TicketVerificationService(GuestService guestService, TicketService ticketService, TicketTypeService ticketTypeService) {
        this.guestService = guestService;
        this.ticketService = ticketService;
        this.ticketTypeService = ticketTypeService;
    }

    @Transactional
    public TicketVerificationStatus verify(String uuid) {
        log.info("Verification ticket: '{}'", uuid);

        Optional<TicketDTO> ticketOpt = ticketService.findByUUID(uuid);

        log.debug("Ticket {}", ticketOpt);

        if (ticketOpt.isPresent()) {
            TicketDTO ticket = ticketOpt.get();

            var guest = ticket.getGuestId() != null ? getGuest(ticket) : null;
            var ticketType = getTicketType(ticket);

            if (ticket.getEnabled()) {
                log.info("Ticket {} is active", ticket);

                if (ticket.getValidatedAt() != null) {
                    log.info("Ticket is already validated");

                    return ticketWasValidatedEarlier(ticket, ticketType, guest);
                } else {
                    validateTicket(ticket);
                    return ticketIsValid(ticket, ticketType, guest);
                }
            } else {
                log.info("Ticket {} is deactivated!", ticket);
                return ticketDeactivated(ticket, ticketType, guest);
            }
        } else {
            return TicketVerificationStatus
                .builder()
                .status(VerificationStatus.NOT_FOUND)
                .message("Nie znaleziono biletu w bazie!!")
                .build();
        }
    }

    private void validateTicket(TicketDTO ticket) {
        log.info("Validating ticket {}", ticket);
        ticketService.validateTicket(ticket.getId());
    }

    private TicketTypeDTO getTicketType(TicketDTO ticket) {
        var ticketTypeOpt = ticketTypeService.findOne(ticket.getTicketTypeId());
        if (ticketTypeOpt.isEmpty()) {
            String msg = String.format("Not found ticket type %s",
                ticket.getTicketTypeId());
            log.error(msg);
        }

        return ticketTypeOpt.orElseGet(() -> null);
    }

    private GuestDTO getGuest(TicketDTO ticket) {
        var guestOpt = guestService.findOne(ticket.getGuestId());
        if (guestOpt.isEmpty()) {
            String msg = String.format("Not found ticket %s guest id #%d ",
                ticket, ticket.getGuestId());
            log.error(msg);
        }

        return guestOpt.orElseGet(() -> null);
    }


    private TicketVerificationStatus ticketIsValid(TicketDTO ticket, TicketTypeDTO ticketType, GuestDTO guest) {
        return TicketVerificationStatus
            .builder()
            .status(VerificationStatus.OK)
            .guest(guest)
            .ticketType(ticketType)
            .message("Bilet jest ważny")
            .build();
    }

    private TicketVerificationStatus ticketWasValidatedEarlier(TicketDTO ticket, TicketTypeDTO ticketType, GuestDTO guest) {
        String validatedAt = formatDateTime(ticket.getValidatedAt());
        String msg = "Bilet jest skasowany. Data: " + validatedAt;

        return TicketVerificationStatus
            .builder()
            .status(VerificationStatus.ALREADY_VALIDATED)
            .guest(guest)
            .ticketType(ticketType)
            .message(msg)
            .build();
    }

    private TicketVerificationStatus ticketDeactivated(TicketDTO ticketDTO, TicketTypeDTO ticketType, GuestDTO guest) {
        String disabledAt = formatDateTime(ticketDTO.getDisabledAt());
        String msg = "Bilet jest nieaktywny!. Deaktywowano go " + disabledAt;

        return TicketVerificationStatus
            .builder()
            .status(VerificationStatus.DEACTIVATED)
            .guest(guest)
            .ticketType(ticketType)
            .message(msg)
            .build();
    }

    private String formatDateTime(Instant instant) {
        if (instant == null) {
            return "";
        } else {
            return instant.truncatedTo(ChronoUnit.MINUTES).toString().replaceAll("[TZ]", " ");
        }
    }
}
