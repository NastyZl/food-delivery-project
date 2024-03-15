package ru.nastyzl.fooddelivery.enums;

public enum OrderStatus {
    ACCEPTED("Принят"),
    DELIVERED("Доставляется"),
    DONE("Выполен"),
    ;
    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
