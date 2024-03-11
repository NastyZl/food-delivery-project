package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = UserRole.Values.COURIER)
public class CourierEntity extends UserEntity {
    private Boolean availability;

    @Override
    public UserRole getRole() {
        return UserRole.COURIER;
    }

    public CourierEntity() {
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}
