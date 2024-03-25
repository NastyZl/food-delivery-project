package ru.nastyzl.fooddelivery.service;

import org.telegram.telegrambots.meta.api.objects.Contact;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.exception.CourierNotFoundException;
import ru.nastyzl.fooddelivery.exception.InvalidRoleException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.CourierEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> findById(Long id);

    UserDto blockUnblockUser(Long id) throws RoleNotFoundException, UserNotFoundException;

    boolean checkAvailable(Long id);

    CourierEntity chooseCourier() throws CourierNotFoundException;

    VendorEntity getVendorByUsername(String username) throws UserNotFoundException;

    CustomerEntity getCustomerByUsername(String username) throws UserNotFoundException;

    UserEntity registerUser(UserDto userDto) throws InvalidRoleException;

    void activateCourier(Contact contact);

    List<UserDto> findAll();

    Optional<CourierEntity> findCourierByChatId(Long id);

    Optional<CourierEntity> findCourierByPhoneNumber(String phone);

    Optional<? extends UserEntity> getByUsername(String username);

}
