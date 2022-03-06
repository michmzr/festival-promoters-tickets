package eu.cybershu.service.dto;

import eu.cybershu.service.ValidationResult;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderImportResult implements Serializable {
    private boolean processed;

    private GuestDTO guestDTO;
    private TicketDTO ticketDTO;

    private OrderRecord orderRecord;
    private ValidationResult validation;

    private Set<String> messages = new HashSet<>();

    public boolean success() {
        return processed && validation.valid();
    }
    public boolean failed() {
        return !success();
    }

    public void addMessage(String message) {
        messages.add(message);
    }
}
