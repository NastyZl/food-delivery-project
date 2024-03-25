package ru.nastyzl.fooddelivery.exception;

public class OrderStatusNotFoundException extends Exception {
    public OrderStatusNotFoundException(String message) {
        super(message);
    }
}
