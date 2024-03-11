package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = UserRole.Values.CUSTOMER)
public class CustomerEntity extends UserEntity {
    @Override
    public UserRole getRole() {
        return UserRole.CUSTOMER;
    }

    public CustomerEntity() {
    }
}
