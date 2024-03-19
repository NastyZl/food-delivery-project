package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nastyzl.fooddelivery.model.CartItemEntity;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    @Query("SELECT item.cart.id FROM CartItemEntity item WHERE item.dish.id = :id")
    List<Long> findCartIdsByDishId(@Param("id") Long dishId);
    @Modifying
    @Query("DELETE FROM CartItemEntity item WHERE item.dish.id = :id")
    void deleteAllByDishId(@Param("id") Long dishId);
    List<CartItemEntity> findByDish_IdAndCart_Id(Long dishId, Long cartId);

}
