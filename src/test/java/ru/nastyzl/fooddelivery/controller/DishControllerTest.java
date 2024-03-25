package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DishController.class)
@AutoConfigureMockMvc(addFilters = false)
class DishControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DishService dishService;

    @MockBean
    Principal principal;

    @MockBean
    CartService cartService;

    @MockBean
    MockMultipartFile multipartFile;

    @MockBean
    BindingResult bindingResult;

    @Test
    public void testCreateDish() throws Exception {
        mockMvc.perform(get("/dish/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("dish/dish-form"))
                .andExpect(model().attributeExists("dishCreateDto"));
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

    @Test
    public void testSaveDish_WhenDishNotValid() throws Exception {
        DishCreateDto dishCreateDto = new DishCreateDto();
        dishCreateDto.setImgPath("dish");

        multipartFile = new MockMultipartFile("image", "test.jpg", "img/test", "image".getBytes());
        Principal principal = () -> "user";
        bindingResult = new BindException(dishCreateDto, "dishCreateDto");


        mockMvc.perform(multipart("/dish/save")
                        .file(multipartFile)
                        .flashAttr("dishCreateDto", dishCreateDto)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("dish/dish-form"));
    }

}