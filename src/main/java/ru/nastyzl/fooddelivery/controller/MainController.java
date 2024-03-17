package ru.nastyzl.fooddelivery.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.service.DishService;

import java.util.List;

@Controller
@RequestMapping
public class MainController {

    private final DishService dishService;

    public MainController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping("/menu")
    public String menu(Model model) {
        List<DishDto> dishes = dishService.findAll();
        model.addAttribute("dishes", dishes);
        model.addAttribute("size", dishes.size());
        return "menu";
    }

    @GetMapping("/test")
    public String test() {
        return "navbar-test";

    }

    @GetMapping("/account-info")
    public String accountInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userDetails", userDetails);
        return "admin/accountInfo";
    }
}
