package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = UserRole.Values.ADMIN)
public class AdminEntity extends UserEntity {
    public AdminEntity() {
    }
}
