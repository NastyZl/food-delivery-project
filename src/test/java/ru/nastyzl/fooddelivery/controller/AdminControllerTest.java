package ru.nastyzl.fooddelivery.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    List<UserDto> users;
    UserDto userDto;
    Long userId;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(new UserDto(), new UserDto());
        userDto = new UserDto();
        userId = 1L;
        userDto.setId(userId);
    }

    @Test
    void testShowAllUser() throws Exception {
        when(userService.findAll()).thenReturn(users);
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/show-users"))
                .andExpect(model().attribute("users", users));
    }

    @Test
    void testBlockUnblockUser() throws Exception {
        when(userService.blockUnblockUser(userId)).thenReturn(userDto);
        mockMvc.perform(get("/admin/block-users/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/users"));
    }

    @Test
    void testShowUserInfo() throws Exception {
        when(userService.findById(userId)).thenReturn(Optional.of(userDto));
        mockMvc.perform(get("/admin/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/accountInfo"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", userDto));
    }
}