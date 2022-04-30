package eu.cybershu.service.dto;

import eu.cybershu.service.OrderCSVFileFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVRecord;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Slf4j
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

    @NotNull
    private String orderId;

    private String couponCode;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal discountPln = BigDecimal.ZERO;

    private String note;

    public static OrderRecord fromCSVRecord(CSVRecord csvRecord) {
        return OrderRecord
            .builder()
            .guestName(getOrDefault(csvRecord, OrderCSVFileFields.BILLING_FIRST_NAME.getFieldName(), ""))
            .guestLastName(getOrDefault(csvRecord, OrderCSVFileFields.BILLING_LAST_NAME.getFieldName(), ""))
            .guestEmail(csvRecord.get(OrderCSVFileFields.USER_EMAIL.getFieldName()))
            .productId(csvRecord.get(OrderCSVFileFields.PRODUCT_ID.getFieldName()))
            .productName(csvRecord.get(OrderCSVFileFields.PRODUCT_NAME.getFieldName()))
            .orderId(csvRecord.get(OrderCSVFileFields.ORDER_ID.getFieldName()))
            .couponCode(getOrDefault(csvRecord, OrderCSVFileFields.COUPON_CODE.getFieldName(), null))
            .price(stringToBigDecimal(csvRecord.get(OrderCSVFileFields.ORDER_TOTAL.getFieldName())))
            .discountPln(stringToBigDecimal(OrderCSVFileFields.DISCOUNT_PRICE.getFieldName()))
            .note(csvRecord.get(OrderCSVFileFields.CUSTOMER_NOTE.getFieldName()))
            .build();
    }

    private static BigDecimal stringToBigDecimal(String val) {
        return BigDecimal.valueOf(Double.parseDouble(val));
    }

    static String getOrDefault(CSVRecord csvRecord, String fieldName, String defaultValue) {
        if (csvRecord.isSet(fieldName)) {
            return csvRecord.get(fieldName);
        } else {
            return defaultValue;
        }
    }

    public BigDecimal getOrderDiscount() {
        return null;
    }
}
