package eu.cybershu.service.dto;

import eu.cybershu.service.ValidationResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderImportResult implements Serializable {
    private GuestDTO guest;
    private TicketDTO ticket;

    private OrderRecord orderRecord;
    private ValidationResult validation;

    private boolean processed;
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
