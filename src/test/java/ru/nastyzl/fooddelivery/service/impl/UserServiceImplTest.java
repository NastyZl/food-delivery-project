package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.CourierNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class UserServiceImplTest {
    @MockBean
    private UserRepository<UserEntity> userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void testRegisterUserForInvalidRole() {
        UserDto userDto = new UserDto();
        userDto.setRole("invalidRole");
        assertThrows(RuntimeException.class, () -> userService.registerUser(userDto));
    }

    @Test
    public void testChooseCourierWithAvailableAndBusyCouriers() throws UserNotFoundException, CourierNotFoundException {
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

        assertThrows(CourierNotFoundException.class, () -> {
            userService.chooseCourier();
        });
    }

}