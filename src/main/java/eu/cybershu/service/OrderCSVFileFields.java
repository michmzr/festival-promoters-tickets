package eu.cybershu.service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum OrderCSVFileFields {
    ORDER_ID("Order ID"),
    CUSTOMER_NOTE("Customer Note"),
    COUPON_CODE("Coupon Code"),
    ORDER_TOTAL("Order Total"),
    CUSTOMER_NAME("Customer Name"),
    SHIPPING_LAST_NAME("Shipping Last Name"),
    USER_EMAIL("User Email"),
    PRODUCT_NAME("Product Name"),
    PRODUCT_ID("Product ID"),
    BILLING_FIRST_NAME("Billing First Name"),
    BILLING_LAST_NAME("Billing Last Name"),
    DISCOUNT_PRICE("Order Discount");

    private final String fieldName;

    OrderCSVFileFields(String fieldName) {
        this.fieldName = fieldName;
    }

    public static Set<String> getFieldNames() {
        return Arrays
            .stream(values())
            .map(OrderCSVFileFields::getFieldName)
            .collect(Collectors.toUnmodifiableSet());
    }

    public String getFieldName() {
        return fieldName;
    }
}
