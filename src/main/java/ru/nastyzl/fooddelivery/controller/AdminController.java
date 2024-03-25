package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.service.UserService;

import javax.management.relation.RoleNotFoundException;

@Controller
@EnableWebMvc
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String showAllUser(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/show-users";
    }

    @GetMapping("/block-users/{id}")
    public String blockUnblockUser(@PathVariable("id") Long id) throws UserNotFoundException, RoleNotFoundException {
        userService.blockUnblockUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}")
    public String showUserInfo(@PathVariable("id") Long id, Model model) throws UserNotFoundException {
        UserDto userDto = userService.findById(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id= " + id + " не найден"));
        model.addAttribute("user", userDto);
        return "admin/accountInfo";
    }

}
