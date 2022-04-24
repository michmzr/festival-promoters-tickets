package eu.cybershu.service.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class OrdersImportResult {
    Set<OrderImportResult> results = new HashSet<>();
    Set<String> errors = new HashSet<>();
    Long imported;
    Long failed;
}
