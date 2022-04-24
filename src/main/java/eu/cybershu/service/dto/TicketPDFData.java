package eu.cybershu.service.dto;

import eu.cybershu.domain.Guest;
import eu.cybershu.domain.Promotor;
import eu.cybershu.domain.TicketType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketPDFData {
    private final String uuid;
    private final byte[] qrFile;
    private final String qrFileContentType;
    private final TicketType ticketType;
    private final Guest guest;
    private final Promotor promotor;
}
