package ru.nastyzl.fooddelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.nastyzl.fooddelivery.bot.service.impl.NotificationServiceImpl;
import ru.nastyzl.fooddelivery.dto.OrderDto;
import ru.nastyzl.fooddelivery.enums.PaymentType;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.exception.CartNotFoundException;
import ru.nastyzl.fooddelivery.exception.OrderNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {
    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;
    private final NotificationServiceImpl notificationServiceImpl;

    public OrderController(UserService userService, OrderService orderService, CartService cartService, NotificationServiceImpl notificationServiceImpl) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartService = cartService;
        this.notificationServiceImpl = notificationServiceImpl;
    }

    @GetMapping("/check-out")
    public String checkOut(Principal principal, @ModelAttribute("order") OrderDto orderDto) throws UserNotFoundException {
        orderDto.setCart(cartService.getCart(principal.getName()));
        return "/order/check-out";
    }

    @PostMapping("/create-order")
    public String createOrder(Principal principal,
                              Model model, @ModelAttribute("order") @Valid OrderDto orderDto, BindingResult bindingResult, @RequestParam("address") String address, @RequestParam("paymentType") PaymentType paymentType) throws UserNotFoundException, CartNotFoundException {
        if (bindingResult.hasErrors()) {
            return "/order/check-out";
        }
        orderDto.setAddress(address);
        orderDto.setPaymentType(paymentType);
        orderDto.setCart(cartService.getCart(principal.getName()));
        OrderEntity order = orderService.save(orderDto);
        notificationServiceImpl.sendNotification(order);
        model.addAttribute("order", order);
        return "redirect:/order/my-orders";
    }

    @GetMapping("/detail/{id}")
    @Transactional
    public String showDetailOfOrder(@PathVariable("id") Long id, Model model) throws OrderNotFoundException {
        Optional<OrderEntity> optionalOrder = orderService.findById(id);
        if (optionalOrder.isPresent()) {
            OrderEntity order = optionalOrder.get();
            List<OrderItemEntity> orderItems = order.getOrderItems();
            model.addAttribute("order", order);
        } else throw new OrderNotFoundException("Информация озаказе удалена");
        return "redirect:/404";
    }


    @GetMapping("/my-orders")
    public String showUserOrders(Principal principal, Model model) throws UserNotFoundException {
        String name = principal.getName();
        Optional<? extends UserEntity> user = userService.getByUsername(name);
        if (user.isPresent()) {
            UserRole role = user.get().getRole();
            if ((role).equals(UserRole.VENDOR)) {
                List<OrderEntity> orders = orderService.findAllForVendor(name);
                model.addAttribute("orders", orders);
            } else if ((role).equals(UserRole.CUSTOMER)) {
                List<OrderEntity> orders = orderService.findAllForCustomer(name);
                model.addAttribute("orders", orders);
            } else throw new UserNotFoundException("Нет доступа к заказам");
        }
        return "/order/my-orders";
    }

}
