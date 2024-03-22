package ru.nastyzl.fooddelivery.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.service.DishService;

import java.security.Principal;

@Controller
@RequestMapping
public class MainController {

    private final DishService dishService;

    public MainController(DishService dishService) {
        this.dishService = dishService;
    }

//    @GetMapping("/menu")
//    public String menu(Model model) {
//        List<DishShowDto> dishes = dishService.getAllDish();
//        model.addAttribute("dishes", dishes);
//        model.addAttribute("size", dishes.size());
//        return "menu";
//    }

    @GetMapping("/404")
    public String error() {
        return "exception/error";
    }

    @GetMapping("/test")
    public String test() {
        return "navbar-test";

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
    public String accountInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("userDetails", userDetails);
        return "admin/accountInfo";
    }
}
