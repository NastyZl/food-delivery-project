package ru.nastyzl.fooddelivery.controller;


import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final DishService dishService;

    private final UserService userService;

    public CartController(CartService cartService, DishService dishService, UserService userService) {
        this.cartService = cartService;
        this.dishService = dishService;
        this.userService = userService;
    }

    @GetMapping("/")
    @Transactional
    public String addToCart(Model model, Principal principal, HttpSession session) throws UserNotFoundException {
        if (principal == null) {
            return "redirect:auth/login";
        }
        CustomerEntity user = userService.getCustomerByUsername(principal.getName());
        CartEntity cart = user.getCart();
        if (cart == null) {
            model.addAttribute("check", "Корзина пустая");
        } else {
            model.addAttribute("grandTotal", cart.getTotalPrise());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("errorMessage", false);

        return "cart/cart-form";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) throws DishNotFoundException, DifferentVendorsException, UserNotFoundException {
        CartEntity cart = cartService.addItemToCart(dishService.getById(id), quantity, principal.getName());
        //  session.setAttribute("totalItems", cart.getTotalItems());
        model.addAttribute("cart", cart);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/update-cart", params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") Integer quantity,
                             Model model,
                             Principal principal) throws UserNotFoundException {
        if (principal == null) {
            return "redirect:/login";
        }
        CartEntity cart = cartService.updateCart(dishService.getById(id), quantity, principal.getName());
        model.addAttribute("cart", cart);
        return "redirect:/cart/";
    }

    @PostMapping(value = "/update-cart", params = "action=delete")
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal,
                             HttpSession session
    ) throws UserNotFoundException {
        if (principal == null) {
            return "redirect:/login";
        } else {
            CartEntity shoppingCart = cartService.removeItemFromCart(dishService.getById(id), principal.getName());
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/cart/";
        }
    }

}
