package ru.nastyzl.fooddelivery.enums;

public enum UserRole {
    ADMIN(Values.ADMIN),
    VENDOR(Values.VENDOR),
    CUSTOMER(Values.CUSTOMER),
    COURIER(Values.COURIER);

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Role{" +
                "value='" + value + '\'' +
                '}';
    }

    public String getValue() {
        return value;
    }

    public static class Values {
        public static final String ADMIN = "admin";
        public static final String VENDOR = "vendor";
        public static final String CUSTOMER = "customer";
        public static final String COURIER = "courier";

    }
}
