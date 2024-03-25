package ru.nastyzl.fooddelivery.service;

import org.springframework.data.domain.Page;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.NullQuantityOfDishesException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Optional<DishEntity> getByDishName(String dishName);

    List<DishShowDto> getAllDish();

    List<DishShowDto> getAllDishesForVendor(Long vendorId);

    DishShowDto getById(Long id);

    DishEntity save(DishCreateDto dishCreateDto, String username) throws UserNotFoundException;

    DishEntity update(DishCreateDto dishCreateDto);

    void changeDeleteFlagById(Long id) throws NullQuantityOfDishesException;

    Page<DishShowDto> pageDishes(int pageNo);

    Optional<DishEntity> findById(Long id);

    List<DishCreateDto> findAll();

    Page<DishShowDto> searchDishes(String keyword, int pageNo);

    Long getVendorIdByDishId(Long id) throws DishNotFoundException;

    DishShowDto dishEntityToDishShowDto(DishEntity dishEntity);

    DishEntity dishShowDtoToDishEntity(DishShowDto dishShowDto);

}
