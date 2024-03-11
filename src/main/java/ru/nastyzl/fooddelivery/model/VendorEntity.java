package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(value = UserRole.Values.VENDOR)
public class VendorEntity extends UserEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "vendorEntity", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<DishEntity> dishes;

    @Override
    public UserRole getRole() {
        return UserRole.VENDOR;
    }

    public VendorEntity() {
    }

    public List<DishEntity> getDishes() {
        return dishes;
    }

    public void setDishes(List<DishEntity> dishes) {
        this.dishes = dishes;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
}
