package ru.nastyzl.fooddelivery.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
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
    public String addToCart(Model model, Principal principal, HttpSession session) {
        if (principal == null) {
            return "redirect:auth/login";
        }
        CustomerEntity user = (CustomerEntity) userService.getByUsername(principal.getName()).get();
        CartEntity cart = user.getCart();
        if (cart == null) {
            model.addAttribute("check", "You don't have any items in your cart");
        } else {
            model.addAttribute("grandTotal", cart.getTotalPrise());
        }

        model.addAttribute("cart", cart);

        // session.setAttribute("totalItems", cart.getTotalItems());

        return "cart/cart-form";
    }

    @PostMapping("/add-to-cart")
    public String addItemToCart(@RequestParam("id") Long id,
                                @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
                                HttpServletRequest request,
                                Model model,
                                Principal principal,
                                HttpSession session) {

        DishDto dishDto = dishService.getById(id);
        String username = principal.getName();
        CartEntity cart = cartService.addItemToCart(dishDto, quantity, username);
        //  session.setAttribute("totalItems", cart.getTotalItems());
        model.addAttribute("cart", cart);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping(value = "/update-cart", params = "action=update")
    public String updateCart(@RequestParam("id") Long id,
                             @RequestParam("quantity") Integer quantity,
                             Model model,
                             Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        DishDto dishDto = dishService.getById(id);
        String username = principal.getName();
        try {
            CartEntity cart = cartService.updateCart(dishDto, quantity, username);
            model.addAttribute("cart", cart);
        } catch (CustomerNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/cart/";
    }

    @PostMapping(value = "/update-cart", params = "action=delete")
    public String deleteItem(@RequestParam("id") Long id,
                             Model model,
                             Principal principal,
                             HttpSession session
    ) {
        if (principal == null) {
            return "redirect:/login";
        } else {
            DishDto dishDto = dishService.getById(id);
            String username = principal.getName();
            CartEntity shoppingCart = null;
            try {
                shoppingCart = cartService.removeItemFromCart(dishDto, username);
            } catch (CustomerNotFoundException e) {
                throw new RuntimeException(e);
            }
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/cart/";
        }
    }
}
