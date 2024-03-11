package ru.nastyzl.fooddelivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.Optional;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
    Optional<DishEntity> findByDishName(String dishName);

}
