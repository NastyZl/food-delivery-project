package ru.nastyzl.fooddelivery.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.service.DishService;

import java.util.Optional;

@Component
public class DishValidator implements Validator {

    private final DishService dishService;

    public DishValidator(DishService dishService) {
        this.dishService = dishService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DishDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DishDto dishDto = (DishDto) target;
        Optional<DishEntity> dish = dishService.getByDishName(dishDto.getDishName());
        if (dish.isPresent()) {
            errors.rejectValue("dishName", "", "Блюдо с таким названием уже существует.");
        }
    }
}
