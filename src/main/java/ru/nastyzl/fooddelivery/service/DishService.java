package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Optional<DishEntity> getByDishName(String dishName);

    DishEntity save(DishDto dishDto);

    DishEntity update(DishDto dishDto);

    void deleteById(Long id);

    List<DishDto> findAll();


}
