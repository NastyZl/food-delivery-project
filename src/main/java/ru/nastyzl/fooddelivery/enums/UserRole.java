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
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String VENDOR = "ROLE_VENDOR";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String COURIER = "ROLE_COURIER";

    }
}
