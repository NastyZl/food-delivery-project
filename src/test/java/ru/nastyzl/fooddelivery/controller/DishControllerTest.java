package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@AutoConfigureMockMvc(addFilters = false)
class DishControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DishService dishService;

    @MockBean
    CartService cartService;

    @Test
    void createDish() {
    }

    @Test
    void saveDish() {
    }

    @Test
    void deleteDish() throws Exception {
        Long dishId = 1L;
        DishShowDto showDto = new DishShowDto();
        showDto.setId(dishId);
        showDto.setDeleted(false);
        when(dishService.getById(dishId)).thenReturn(showDto);
        doNothing().when(cartService).removeDishFromCarts(dishId);

        doNothing().when(dishService).changeDeleteFlagById(dishId);

        mockMvc.perform(delete("/dish/{id}", dishId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/vendor/dishes"));

        verify(dishService, times(1)).changeDeleteFlagById(dishId);

        verify(cartService, times(1)).removeDishFromCarts(dishId);
    }

}