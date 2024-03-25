package ru.nastyzl.fooddelivery.exception;

public class CourierNotFoundException extends Exception {
    public CourierNotFoundException(String message) {
        super(message);
    }
}
