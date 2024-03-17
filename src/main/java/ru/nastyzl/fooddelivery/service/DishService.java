package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Optional<DishEntity> getByDishName(String dishName);

    DishDto getById(Long id);

    DishEntity save(DishDto dishDto, Long vendorId);

    DishEntity update(DishDto dishDto);

    void deleteById(Long id);

    List<DishDto> findAll();

    Long getVendorIdByDishId(Long id) throws DishNotFoundException;

}
