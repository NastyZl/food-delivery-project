package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.PaymentType;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(UserService userService, OrderService orderService, CartService cartService) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/check-out")
    public String checkOut(Principal principal, @ModelAttribute("order") OrderDto orderDto) throws UserNotFoundException {
        orderDto.setCart(cartService.getCart(principal.getName()));
        return "order/check-out";
    }

    @PostMapping("/create-order")
    public String createOrder(Principal principal,
                              Model model, @ModelAttribute("order") OrderDto orderDto, @RequestParam("address") String address, @RequestParam("paymentType") PaymentType paymentType) throws UserNotFoundException {
        orderDto.setAddress(address);
        orderDto.setPaymentType(paymentType);
        orderDto.setCart(cartService.getCart(principal.getName()));
        OrderEntity order = orderService.save(orderDto);
        model.addAttribute("order", order);
        return "order/detail";
    }

}
