package ru.nastyzl.fooddelivery.exception;

public class MaxQuantityExceededException extends Exception{
    public MaxQuantityExceededException(String message) {
        super(message);
    }
}
