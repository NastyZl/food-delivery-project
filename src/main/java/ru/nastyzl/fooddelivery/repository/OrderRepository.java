package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
