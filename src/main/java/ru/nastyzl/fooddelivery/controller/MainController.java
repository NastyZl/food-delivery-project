package ru.nastyzl.fooddelivery.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping
public class MainController {
    private final UserService userService;

    private final DishService dishService;

    public MainController(UserService userService, DishService dishService) {
        this.userService = userService;
        this.dishService = dishService;
    }

    @GetMapping("/404")
    public String error() {
        return "exception/error";
    }


    @GetMapping("menu/{pageNo}")
    public String dishPage(@PathVariable("pageNo") int pageNo, Model model) {
        Page<DishShowDto> dishes = dishService.pageDishes(pageNo);
        model.addAttribute("size", dishes.getSize());
        model.addAttribute("totalPages", dishes.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("dishes", dishes);
        return "menu";
    }

    @GetMapping("/menu/search-result/{pageNo}")
    public String searchDishes(@PathVariable("pageNo") int pageNo,
                               @RequestParam("keyword") String keyword,
                               Model model) {
        Page<DishShowDto> dishes = dishService.searchDishes(keyword, pageNo);
        model.addAttribute("size", dishes.getSize());
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPages", dishes.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("dishes", dishes);
        return "search-result-menu";
    }

    @GetMapping("/account-info")
    public String accountInfo(Model model, Principal principal) {
        Optional<? extends UserEntity> optionalUser = userService.getByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();
            model.addAttribute("user", user);
        } else return "redirect:/auth/login";
        return "admin/accountInfo";
    }
}
