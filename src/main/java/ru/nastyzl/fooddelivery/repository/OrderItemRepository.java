package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.OrderItemEntity;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
