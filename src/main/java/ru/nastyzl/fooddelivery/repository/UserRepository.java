package ru.nastyzl.fooddelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.model.*;

import java.util.List;
import java.util.Optional;


public interface UserRepository<T extends UserEntity> extends JpaRepository<T, Long> {
    @Query("from VendorEntity")
    List<VendorEntity> findAllVendor();

    @Query("from AdminEntity")
    List<AdminEntity> findAllAdmin();

    @Query("from CustomerEntity")
    List<CustomerEntity> findAllCustomer();

    @Query("from CourierEntity")
    List<CourierEntity> findAllCourier();
    @Query("from CourierEntity c where c.chatId=:chatId")
    Optional<CourierEntity> findByChatId(@Param("chatId") Long chatId);

    Optional<? extends UserEntity> findByUsername(String username);

}
