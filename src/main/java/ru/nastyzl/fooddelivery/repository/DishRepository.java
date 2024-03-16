package ru.nastyzl.fooddelivery.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nastyzl.fooddelivery.model.DishEntity;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<DishEntity, Long> {
    Optional<DishEntity> findByDishName(String dishName);

    @Query("SELECT d FROM DishEntity d WHERE d.vendorEntity.id = :vendorId")
    List<DishEntity> findDishesByVendorId(@Param("vendorId") Long vendorId);

}
