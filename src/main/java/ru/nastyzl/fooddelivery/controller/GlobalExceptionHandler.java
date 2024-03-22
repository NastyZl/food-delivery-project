package ru.nastyzl.fooddelivery.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nastyzl.fooddelivery.exception.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DifferentVendorsException.class})
    public String handleDifferentVendorsException(DifferentVendorsException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "В Вашей корзине уже есть блюда другого повара.\n" +
                "Чтобы положить выбранный товар в корзину, необходимо самостоятельно очисть корзину.\n" +
                "(мы работаем над этим)");
        return "redirect:/menu/0";
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
    @ExceptionHandler({MaxQuantityExceededException.class})
    public String handleMaxQuantityExceededException(MaxQuantityExceededException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("maxQuantityErrorMessage", ex.getMessage());
        return "redirect:/cart/";
    }
    @ExceptionHandler({NullQuantityOfDishesException.class})
    public String handleNullQuantityOfDishesException(NullQuantityOfDishesException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("nullQuantityErrorMessage", ex.getMessage());
        return "redirect:/vendor/dishes";
    }
}
