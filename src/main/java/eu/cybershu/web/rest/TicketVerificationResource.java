package eu.cybershu.web.rest;

import eu.cybershu.service.TicketVerificationService;
import eu.cybershu.service.dto.TicketVerificationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TicketVerificationResource {
    private final TicketVerificationService ticketVerificationService;

    public TicketVerificationResource(TicketVerificationService ticketVerificationService) {
        this.ticketVerificationService = ticketVerificationService;
    }

    @GetMapping("/api/ticket/verify/{uuid}")
    public ResponseEntity<TicketVerificationStatus> verifyTicket(@PathVariable String uuid) {
        log.info("Checking ticket '{}'", uuid);

        TicketVerificationStatus status = ticketVerificationService.verify(uuid);

        log.info("Returning {} for uuid: {}", status, uuid);

        return ResponseEntity.ok().body(status);
    }
}
