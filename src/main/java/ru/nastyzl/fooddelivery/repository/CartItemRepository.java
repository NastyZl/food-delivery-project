package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.CartItemEntity;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
}
