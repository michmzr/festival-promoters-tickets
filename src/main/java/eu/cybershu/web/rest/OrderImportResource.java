package eu.cybershu.web.rest;

import eu.cybershu.service.OrdersImportService;
import eu.cybershu.service.dto.OrdersImportResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;



@RestController
@RequestMapping("/api/import/")
public class OrderImportResource {
    private OrdersImportService ordersImportService;

    /**
     * Import Orders CSV from Woocomerce
     *
     * @param file               Source file
     * @param redirectAttributes
     * @return
     */
    //todo expect CSV file format
    @RequestMapping(path = "/orders", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<OrdersImportResult> importOrders(@RequestParam("file") MultipartFile file,
                                                           RedirectAttributes redirectAttributes) throws IOException {

        OrdersImportResult ordersImportResult = ordersImportService.loadRecords(file);

        return ResponseEntity.ok()
            .body(ordersImportResult);
    }
}
