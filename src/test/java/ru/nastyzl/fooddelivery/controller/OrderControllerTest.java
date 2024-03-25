package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import ru.nastyzl.fooddelivery.bot.service.impl.NotificationServiceImpl;
import ru.nastyzl.fooddelivery.model.CartEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.OrderEntity;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.OrderService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @MockBean
    Principal principal;

    @MockBean
    UserService userService;

    @MockBean
    NotificationServiceImpl notificationService;

    @MockBean
    OrderService orderService;

    @MockBean
    BindingResult bindingResult;

    @Test
    void testCheckOut() throws Exception {
        principal = () -> "user";
        CartEntity cartEntity = new CartEntity();
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstName("firstname");
        customerEntity.setLastName("lastname");
        customerEntity.setPhone("phone");
        cartEntity.setCustomer(customerEntity);

        when(cartService.getCart(anyString())).thenReturn(cartEntity);

        mockMvc.perform(get("/order/check-out")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("order"));
    }

    @Test
    public void testShowDetailOfOrder() throws Exception {
        OrderEntity order = new OrderEntity();
        List<OrderItemEntity> orderItems = new ArrayList<>();
        order.setOrderItems(orderItems);

        when(orderService.findById(anyLong())).thenReturn(Optional.of(order));

        mockMvc.perform(get("/order/detail/{id}", 1L))
                .andExpect(redirectedUrl("/404"));

        verify(orderService).findById(1L);
    }

    @Test
    public void testShowDetailOfOrder_ThrowsOrderNotFoundException() throws Exception {
        when(orderService.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/detail/{id}", 1L))
                .andExpect(status().isNotFound());
        verify(orderService, times(0)).findById(anyLong());
    }

}