package ru.nastyzl.fooddelivery.exception;

public class DishNotFoundException extends Exception {
    public DishNotFoundException(String messages) {
        super(messages);
    }

    public DishNotFoundException() {
    }
}

