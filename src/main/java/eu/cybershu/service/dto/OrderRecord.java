package eu.cybershu.service.dto;

import eu.cybershu.service.OrderCSVFileFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRecord {
    @NotNull
    private String guestName;
    @NotNull
    private String guestLastName;
    @NotNull
    @Email
    private String guestEmail;

    private String productName;

    @NotNull
    private String productId;

    private long orderId;

    @NotEmpty
    private String couponCode;

    @NotEmpty
    private String price;

    private String note;

    public static OrderRecord fromCSVRecord(CSVRecord csvRecord) {
        return OrderRecord
            .builder()
            .guestName(csvRecord.get(OrderCSVFileFields.BILLING_FIRST_NAME.getFieldName()))
            .guestLastName(csvRecord.get(OrderCSVFileFields.BILLING_LAST_NAME.getFieldName()))
            .guestEmail(csvRecord.get(OrderCSVFileFields.USER_EMAIL.getFieldName()))
            .productId(csvRecord.get(OrderCSVFileFields.PRODUCT_ID.getFieldName()))
            .productName(csvRecord.get(OrderCSVFileFields.PRODUCT_NAME.getFieldName()))
            .orderId(Long.parseLong(OrderCSVFileFields.ORDER_ID.getFieldName()))
            .couponCode(csvRecord.get(OrderCSVFileFields.COUPON_CODE.getFieldName()))
            .price(csvRecord.get(OrderCSVFileFields.ORDER_TOTAL.getFieldName()))
            .note(csvRecord.get(OrderCSVFileFields.CUSTOMER_NOTE.getFieldName()))
            .build();
    }

}
