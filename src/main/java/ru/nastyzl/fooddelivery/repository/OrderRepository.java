package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nastyzl.fooddelivery.model.OrderEntity;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("select o from OrderEntity o where o.customer.id=:id")
    List<OrderEntity> findAllByCustomerId(@Param("id")Long id);

    @Query("select o from OrderEntity o where o.vendor.id=:id")
    List<OrderEntity> findAllByVendorId(@Param("id")Long id);

}
