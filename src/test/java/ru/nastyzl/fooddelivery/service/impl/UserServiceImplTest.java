package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository<UserEntity> userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testActivateCourier() {
//        String username = "courier";
//        Long chatId = 12345L;
//
//        CourierEntity user = new CourierEntity();
//        user.setUsername(username);
//        user.setChatId(null);
//        user.setAvailability(true);
//
//        when(userRepository.findAllCourier()).thenReturn(List.of(user));
//
//        boolean result = userService.activateCourier(username, chatId);
//
//        assertTrue(result);
//        assertEquals(chatId, user.getChatId());
//        assertFalse(user.isAvailable());
//        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testRegisterUserForInvalidRole() {
        UserDto userDto = new UserDto();
        userDto.setRole("invalidRole");
        assertThrows(RuntimeException.class, () -> userService.registerUser(userDto));
    }

    @Test
    public void testChooseCourierWithAvailableAndBusyCouriers() throws UserNotFoundException {
        CourierEntity courier1 = new CourierEntity();
        courier1.setAvailability(true);
        courier1.setChatId(1L);

        CourierEntity courier2 = new CourierEntity();
        courier2.setAvailability(false);
        courier2.setChatId(2L);

        when(userRepository.findAllCourier()).thenReturn(Arrays.asList(courier1, courier2
        ));

        CourierEntity chosenCourier = userService.chooseCourier();
        assertNotNull(chosenCourier);
    }

    @Test
    public void testChooseCourierWithoutActivatedCouriers() {

        CourierEntity courier1 = new CourierEntity();
        courier1.setAvailability(true);
        courier1.setChatId(null);

        CourierEntity courier2 = new CourierEntity();
        courier2.setAvailability(false);
        courier2.setChatId(null);

        when(userRepository.findAllCourier()).thenReturn(Arrays.asList(courier1, courier2
        ));

        assertThrows(UserNotFoundException.class, () -> {
            userService.chooseCourier();
        });
    }

}