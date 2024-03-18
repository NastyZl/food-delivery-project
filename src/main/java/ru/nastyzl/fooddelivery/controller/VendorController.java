package ru.nastyzl.fooddelivery.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

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
    public String showAllDishes(Authentication authentication, Model model) throws CustomerNotFoundException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        Long vendorId = userService.getVendorByUsername(principal.getUsername()).getId();
        model.addAttribute("dishes", dishService.getAllDishesForVendor(vendorId));
        return "vendor/show-dishes";
    }

}
