package eu.cybershu.service;

import eu.cybershu.service.dto.OrderImportResult;
import eu.cybershu.service.dto.OrderRecord;
import eu.cybershu.service.dto.OrdersImportResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class OrdersImportService {
    private final OrderImportJob orderImportJob;

    private final List<String> expectedColumns = new ArrayList<>(OrderCSVFileFields.getFieldNames());

    public OrdersImportService(OrderImportJob orderImportJob) {
        this.orderImportJob = orderImportJob;
    }

    public OrdersImportResult loadRecords(MultipartFile file) throws IOException {
        log.info("Loading records from file: name:{}, original name:{}, bytes:{}", file.getName(), file.getOriginalFilename(), file.getSize());

        OrdersImportResult ordersImportResult = new OrdersImportResult();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            log.info("Header fields: {}", csvParser.getHeaderNames());

            ValidationResult headerValidation = validateHeader(csvParser);
            log.info("Header validation result: {}", headerValidation);

            if (headerValidation.valid) {
                List<CSVRecord> csvRecords = csvParser.getRecords();
                log.info("Found {} lines", csvRecords.size());

                long imported = 0;
                long failed = 0;
                for (CSVRecord csvRecord : csvRecords) {
                    OrderImportResult importResult = orderImportJob.processRecord(
                        OrderRecord.fromCSVRecord(csvRecord));

                    ordersImportResult.getResults().add(importResult);

                    if (importResult.success()) {
                        imported++;
                    }

                    if (importResult.failed()) {
                        failed++;
                    }

                }

                ordersImportResult.setImported(imported);
                ordersImportResult.setFailed(failed);

                return ordersImportResult;
            } else {
                ordersImportResult.setImported(0L);
                ordersImportResult.setFailed((long) csvParser.getRecords().size());
                ordersImportResult.setErrors(headerValidation.messages);
                ordersImportResult.setResults(Collections.emptySet());
            }

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());

            //todo
        }

        return ordersImportResult;
    }

    public static CSVParser readCsvRecords(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                 CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            return csvParser;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private ValidationResult validateHeader(CSVParser csvParser) {

        List<String> fileHeaderNames = csvParser.getHeaderNames();

        if (expectedColumns.equals(fileHeaderNames)) {
            return ValidationResult.ok();
        } else {
            ValidationResult validationResult = new ValidationResult();

            String msg = String.format("Mismatch header fields.\nFile:\n %s, \nexpected:\n%s", fileHeaderNames, expectedColumns);
            log.error(msg);

            validationResult.addMessage(msg);

            return validationResult;
        }
    }
}
