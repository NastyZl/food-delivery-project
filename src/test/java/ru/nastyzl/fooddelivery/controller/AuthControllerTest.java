package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.service.UserService;
import ru.nastyzl.fooddelivery.util.UserValidator;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    BindingResult bindingResult;

    @MockBean
    UserValidator userValidator;
    List<UserDto> users;
    UserDto userDto;
    Long userId;
    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(new UserDto(), new UserDto());
        userDto = new UserDto();
        userId = 1L;
        userDto.setId(userId);
        userDto.setRole(UserRole.VENDOR.getValue());
        userEntity = new VendorEntity();
    }

    @Test
    void testRegistrationPage() throws Exception {
        mockMvc.perform(get("/auth/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"));
    }

    @Test
    void testPerformRegistration_WithInvalidInput() throws Exception {
        UserDto invalidUser = new UserDto();
        invalidUser.setUsername("user");
        invalidUser.setRole(UserRole.CUSTOMER.getValue());
        BindingResult bindingResult = new org.springframework.validation.BeanPropertyBindingResult(invalidUser, "user");
        bindingResult.addError(new FieldError("user", "username", "Username cannot be empty"));

        doNothing().when(userValidator).validate(any(UserDto.class), any(BindingResult.class));

        mockMvc.perform(post("/auth/registration")
                        .flashAttr("user", invalidUser)
                        .sessionAttr("org.springframework.validation.BindingResult.user", bindingResult))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/registration"));

        verify(userService, never()).registerUser(any(UserDto.class));
    }
}