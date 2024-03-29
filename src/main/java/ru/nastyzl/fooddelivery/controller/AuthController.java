package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.exception.InvalidRoleException;
import ru.nastyzl.fooddelivery.service.UserService;
import ru.nastyzl.fooddelivery.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator userValidator;
    private final UserService userService;

    public AuthController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDto user) {
        return "auth/registration";
    }

    @Transactional
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid UserDto user, BindingResult bindingResult) throws InvalidRoleException {
        userValidator.validate(user, bindingResult);
        if (user.getRole().equals(UserRole.VENDOR) && bindingResult.hasErrors()) {
            return "/auth/registration";
        } else if (bindingResult.hasFieldErrors("username") || bindingResult.hasFieldErrors("email")
                || bindingResult.hasFieldErrors("firstName") || bindingResult.hasFieldErrors("lastName")
                || bindingResult.hasFieldErrors("phone") || bindingResult.hasFieldErrors("password")) {
            return "/auth/registration";
        }
        userService.registerUser(user);
        return "redirect:/auth/login";
    }
}
