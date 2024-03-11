package ru.nastyzl.fooddelivery.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.service.UserService;

@Controller
@RequestMapping("/vendor")
public class VendorController {

    private final UserService userService;

    public VendorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dishes")
    public String showAllDishes(Authentication authentication, Model model) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        model.addAttribute("dishes", userService.getAllDishes(principal.getUsername()));
        return "vendor/show-dishes";
    }

}
