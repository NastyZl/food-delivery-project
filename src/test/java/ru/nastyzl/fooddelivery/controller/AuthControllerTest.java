package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.exception.InvalidRoleException;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.service.UserService;
import ru.nastyzl.fooddelivery.util.UserValidator;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void testLoginPage() throws Exception {
        mockMvc.perform(post("/auth/login"))

                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    void registrationPage() {
    }

    @Test
    void testPerformRegistration() throws Exception {
        final MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/registration");
       doNothing().when(userValidator).validate(any(), any());
       when(bindingResult.hasFieldErrors()).thenReturn(false);
       when(bindingResult.hasErrors()).thenReturn(false);
       when(userService.registerUser(userDto)).thenReturn(userEntity);
        mockMvc.perform(mockHttpServletRequestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/auth/login"));

    }
}