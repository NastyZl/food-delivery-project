package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.CartEntity;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
