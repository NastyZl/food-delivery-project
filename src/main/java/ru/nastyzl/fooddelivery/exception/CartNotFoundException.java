package ru.nastyzl.fooddelivery.exception;

public class CartNotFoundException extends Exception {
    public CartNotFoundException(String message) {
        super(message);
    }
}
