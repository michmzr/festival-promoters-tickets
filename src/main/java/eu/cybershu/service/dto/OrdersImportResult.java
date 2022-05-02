package eu.cybershu.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OrdersImportResult {
    List<OrderImportResult> results = new ArrayList<>();
    Set<String> errors = new HashSet<>();
    Long imported;
    Long failed;
}
