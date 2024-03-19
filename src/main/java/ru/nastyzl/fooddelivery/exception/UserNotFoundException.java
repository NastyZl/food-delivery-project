package ru.nastyzl.fooddelivery.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String customerNotFound) {
        super(customerNotFound);
    }
}
