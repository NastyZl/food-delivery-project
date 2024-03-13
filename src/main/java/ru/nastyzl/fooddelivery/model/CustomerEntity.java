package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = UserRole.Values.CUSTOMER)
public class CustomerEntity extends UserEntity {

    @Transient
    private final UserRole role = UserRole.CUSTOMER;

    @Override
    public UserRole getRole() {
        return role;
    }

    public CustomerEntity() {
    }
}
