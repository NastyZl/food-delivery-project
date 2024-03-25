package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    private final UserService userService;
    private final DishService dishService;

    public VendorController(UserService userService, DishService dishService) {
        this.userService = userService;
        this.dishService = dishService;
    }

    @GetMapping("/dishes")
    public String showAllDishes(Principal principal, Model model) throws UserNotFoundException {
        Long vendorId = userService.getVendorByUsername(principal.getName()).getId();
        model.addAttribute("dishes", dishService.getAllDishesForVendor(vendorId));
        return "vendor/show-dishes";
    }

}
