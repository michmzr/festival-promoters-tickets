package eu.cybershu.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptySet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidationResult {
    Set<String> messages = new HashSet<>();
    Set<String> fieldNames = new HashSet<>();
    Boolean valid = true;

    public static ValidationResult ok() {
        return new ValidationResult(emptySet(), emptySet(), true);
    }

    public void setMessages(Set<String> messages) {
        this.messages = messages;
        valid = false;
    }

    public void addMessage(String message) {
        this.messages.add(message);
        this.valid = false;
    }

    public void setFieldNames(Set<String> fieldNames) {
        this.fieldNames = fieldNames;
        valid = false;
    }

    public void addFieldName(String fieldName) {
        this.fieldNames.add(fieldName);
        this.valid = false;
    }

    public void invalidField(String fieldName, String message) {
        addFieldName(fieldName);
        addMessage(message);
        this.valid = false;
    }

    public boolean valid() {
        return valid;
    }

    public boolean failed() {
        return !valid;
    }
}
