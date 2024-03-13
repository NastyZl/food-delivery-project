package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = UserRole.Values.ADMIN)
public class AdminEntity extends UserEntity {
    @Transient
    private final UserRole role = UserRole.ADMIN;
    @Override
    public UserRole getRole() {
        return role;
    }

    public AdminEntity() {
    }
}
