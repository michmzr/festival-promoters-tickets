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
    private OrdersImportService ordersImportService;

    /**
     * Import Orders CSV from Woocomerce
     *
     * @param file Source file
     * @return
     */
    //todo expect CSV file format
    @RequestMapping(path = "/orders", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<OrdersImportResult> importOrders(@RequestPart("file") MultipartFile file) throws IOException {
        //todo musi być CSV
        OrdersImportResult ordersImportResult = ordersImportService.loadRecords(file);

        return ResponseEntity.ok()
            .body(ordersImportResult);
    }
}
