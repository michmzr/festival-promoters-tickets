package eu.cybershu.service;

import eu.cybershu.service.dto.*;
import lombok.SneakyThrows;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class OrdersImportServiceTest {
    private OrdersImportService ordersImportService;
    private final OrderImportJob orderImportJob= mock(OrderImportJob.class);

    @BeforeEach
    void setUp() {
        ordersImportService = new OrdersImportService(orderImportJob);
    }

    @Test
    @Disabled
    @SneakyThrows
    void given_file_expect_no_error() {
        //given
        MultipartFile inputFile = loadFile("data/orders/woocommerce_valid_file01.csv");

        //when
        given(orderImportJob.processRecord(any())).willAnswer(new Answer<OrderImportResult>() {
            @Override
            public OrderImportResult answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return mockImported((CSVRecord) args[0]);
            }
        });
        OrdersImportResult result = ordersImportService.loadRecords(inputFile);

        //then
        verify(orderImportJob, times(2)).processRecord(any());

        assertThat(result.getResults()).hasSize(2);
        assertThat(result.getResults()).allMatch(OrderImportResult::success);

        assertThat(result.getImported()).isEqualTo(2);
        assertThat(result.getFailed()).isZero();

        assertThat(result.getErrors()).isEmpty();
    }

    @Test
    @SneakyThrows
    void given_file_with_no_required_header_expect_import_failed() {
        //given
        MultipartFile inputFile = loadFile("data/orders/woocommerce_invalid_header_mismatch_file01.csv");

        //when
        OrdersImportResult result = ordersImportService.loadRecords(inputFile);

        //then
        verifyNoInteractions(orderImportJob);

        assertThat(result.getResults()).isEmpty();
        assertThat(result.getImported()).isEqualTo(0);
        assertThat(result.getFailed()).isEqualTo(2);

        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().toString()).contains("Mismatch header fields");
    }

    @SneakyThrows
    MultipartFile loadFile(String path) {
        var resource = new ClassPathResource(path);
        FileInputStream fileInputStream = new FileInputStream(resource.getFile());

        return new MockMultipartFile(path, fileInputStream);
    }

    OrderImportResult mockImported(CSVRecord csvRecord) {
        OrderImportResult importResult = new OrderImportResult();

        importResult.setProcessed(true);
        importResult.setOrderRecord(
            OrderRecord.fromCSVRecord(csvRecord)
        );
        importResult.setValidation(ValidationResult.ok());
        importResult.setGuest(mock(GuestDTO.class));
        importResult.setTicket(mock(TicketDTO.class));

        return importResult;
    }
}
