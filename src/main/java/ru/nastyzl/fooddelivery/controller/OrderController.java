package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;

    public OrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/check-out")
    public String checkOut(Model model, Principal principal) {
        CustomerEntity user = (CustomerEntity) userService.getByUsername(principal.getName()).get();
        model.addAttribute("customer", user);
        CartEntity cart = user.getCart();
        model.addAttribute("cart", cart);
        return "order/check-out";
    }

    @PostMapping("/create-order")
    public String createOrder(Principal principal,
                              Model model) throws CustomerNotFoundException {
        CustomerEntity customer = (CustomerEntity) userService.getByUsername(principal.getName()).get();
        CartEntity cart = customer.getCart();
        OrderEntity order = orderService.save(cart);
        model.addAttribute("order", order);
        return "order/detail";
    }

}
