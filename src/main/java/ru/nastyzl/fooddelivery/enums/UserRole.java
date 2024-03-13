package ru.nastyzl.fooddelivery.enums;

public enum UserRole {
    ADMIN(Values.ADMIN, "Администратор"),
    VENDOR(Values.VENDOR, "Повар"),
    CUSTOMER(Values.CUSTOMER, "Покупатель"),
    COURIER(Values.COURIER, "Курьер");

    private final String value;
    private final String ruValue;

    UserRole(String value, String ruValue) {
        this.value = value;
        this.ruValue = ruValue;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public String getRuValue() {
        return ruValue;
    }

    public static class Values {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String VENDOR = "ROLE_VENDOR";
        public static final String CUSTOMER = "ROLE_CUSTOMER";
        public static final String COURIER = "ROLE_COURIER";

    }
}
