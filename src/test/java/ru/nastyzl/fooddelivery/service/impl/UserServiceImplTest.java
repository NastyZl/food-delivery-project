package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class UserServiceImplTest {

    @Mock
    private UserRepository<? extends UserEntity> userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetByUsername() {
        String username = "customer";
        CustomerEntity user = new CustomerEntity();
        user.setUsername(username);

//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<? extends UserEntity> result = userService.getByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }
}