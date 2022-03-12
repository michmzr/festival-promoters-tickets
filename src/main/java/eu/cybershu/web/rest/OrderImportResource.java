package eu.cybershu.web.rest;

import eu.cybershu.service.OrdersImportService;
import eu.cybershu.service.dto.OrdersImportResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/api/import/")
public class OrderImportResource {
    private final OrdersImportService ordersImportService;

    public OrderImportResource(OrdersImportService ordersImportService) {
        this.ordersImportService = ordersImportService;
    }

    /**
     * Import Orders CSV from Woocomerce
     *
     * @param file Source file
     * @return
     */
    @RequestMapping(path = "/orders", method = RequestMethod.POST,
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OrdersImportResult> importOrders(@RequestPart("file") MultipartFile file) throws IOException {
        OrdersImportResult ordersImportResult = ordersImportService.loadRecords(file);

        //todo expect CSV file format

        return ResponseEntity.ok()
            .body(ordersImportResult);
    }
}
