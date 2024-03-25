package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendorController.class)
@AutoConfigureMockMvc(addFilters = false)
class VendorControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    DishService dishService;

    @MockBean
    Principal principal;

    @Test
    void testShowAllDishes() throws Exception {
        principal = () -> "user";
        Long vendorId = 1L;
        List<DishShowDto> dishes = new ArrayList<>();
        VendorEntity vendor = new VendorEntity();
        vendor.setId(vendorId);

        when(userService.getVendorByUsername(principal.getName())).thenReturn(vendor);
        when(dishService.getAllDishesForVendor(vendorId)).thenReturn(dishes);

        mockMvc.perform(get("/vendor/dishes").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("vendor/show-dishes"))
                .andExpect(model().attributeExists("dishes"));

    }
}