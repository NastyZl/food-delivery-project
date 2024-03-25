package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.CartItemEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    Principal principal;


    @MockBean
    CartService cartService;

    @MockBean
    DishService dishService;

    @Test
    void showCart() throws Exception {
        String username = "user";
        CustomerEntity user = new CustomerEntity();
        CartEntity cart = new CartEntity();
        cart.setCartItems(Set.of(new CartItemEntity()));
        user.setCart(cart);
        user.setUsername(username);

        when(principal.getName()).thenReturn(username);
        when(userService.getCustomerByUsername(username)).thenReturn(user);

        mockMvc.perform(get("/cart/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart-form"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(model().attribute("errorMessage", false));
    }

    @Test
    void deleteItemTest() throws Exception {
        String username = "user";
        CustomerEntity user = new CustomerEntity();
        CartEntity shoppingCart = new CartEntity();
        shoppingCart.setCartItems(Set.of(new CartItemEntity()));
        user.setCart(shoppingCart);
        user.setUsername(username);
        Long dishId = 1L;

        DishShowDto dish = new DishShowDto();
        dish.setId(dishId);

        when(principal.getName()).thenReturn(username);
        when(dishService.getById(dishId)).thenReturn(dish);
        when(cartService.removeItemFromCart(dish, username))
                .thenReturn(shoppingCart);

        mockMvc.perform(post("/cart/update-cart")
                        .param("id", "1")
                        .param("action", "delete")
                        .principal(principal))
                .andExpect(redirectedUrl("/cart/"));

        verify(cartService, times(1))
                .removeItemFromCart(any(), anyString());
    }

    @Test
    void testAddItemToCart() throws Exception {
        String username = "user";
        int quantity = 1;
        Long dishId = 1L;

        DishShowDto dish = new DishShowDto();
        CartEntity cart = new CartEntity();
        cart.setCustomer(new CustomerEntity());
        cart.setId(1L);

        when(principal.getName()).thenReturn(username);
        when(dishService.getById(dishId)).thenReturn(dish);
        when(cartService.addItemToCart(dish, quantity, username)).thenReturn(cart);

        mockMvc.perform(post("/cart/add-to-cart")
                        .param("id", String.valueOf(dishId))
                        .param("quantity", String.valueOf(quantity))
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/menu/0"));

        verify(cartService, times(1))
                .addItemToCart(dish, quantity, username);
    }

}