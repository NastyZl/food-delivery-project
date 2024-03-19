package ru.nastyzl.fooddelivery.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DifferentVendorsException.class})
    public String handleDifferentVendorsException(DifferentVendorsException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Вы пытаетесь добавить блюдо другого повара (либо чистим козину, либо оставляем все как есть)");
        return "redirect:/menu";
    }

    @ExceptionHandler({DishNotFoundException.class})
    public String handleDishesNotFoundException(DishNotFoundException ex) {
        return "redirect:/menu";
    }

    @ExceptionHandler({UserNotFoundException.class})
    public String handleUserNotFoundException(UserNotFoundException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", ex);
        return "redirect:/404";
    }
}
