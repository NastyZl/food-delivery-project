package ru.nastyzl.fooddelivery.enums;

public enum PaymentType {
    CASH("Наличные"),
    CART_TO_COURIER("Картой курьеру"),
    ;
    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
