package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nastyzl.fooddelivery.model.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
}
