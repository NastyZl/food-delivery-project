package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Optional<DishEntity> getByDishName(String dishName);

    List<DishShowDto> getAllDish();

    List<DishShowDto> getAllDishesForVendor(Long vendorId);

    DishShowDto getById(Long id);

    DishEntity save(DishCreateDto dishCreateDto, String username) throws CustomerNotFoundException;

    DishEntity update(DishCreateDto dishCreateDto);

    void deleteById(Long id);

    List<DishCreateDto> findAll();

    Long getVendorIdByDishId(Long id) throws DishNotFoundException;

    DishShowDto dishEntityToDishShowDto(DishEntity dishEntity);

    DishEntity dishShowDtoToDishEntity(DishShowDto dishShowDto);

}
