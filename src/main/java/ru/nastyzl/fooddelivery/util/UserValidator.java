package ru.nastyzl.fooddelivery.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Optional;

/**
 * Implements the Validator interface to validate the input for creating a new user.
 */
@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDto.class.equals(clazz);
    }

    /**
     * Validates the input and adds errors if the username already exists
     * @param target the object to be validated
     * @param errors the Errors object to add validation errors
     */
    @Override
    public void validate(Object target, Errors errors) {
        UserDto userDto = (UserDto) target;
        Optional<? extends UserEntity> user = userService.getByUsername(userDto.getUsername());
        if (user.isPresent()) {
            errors.rejectValue("username", "", "Человек с таким именем пользователя уже существует.");
        }
    }
}
