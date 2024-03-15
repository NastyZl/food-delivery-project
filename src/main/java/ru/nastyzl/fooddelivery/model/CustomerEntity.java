package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = UserRole.Values.CUSTOMER)
public class CustomerEntity extends UserEntity {

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CartEntity cart;
    @Transient
    private final UserRole role = UserRole.CUSTOMER;

    @Override
    public UserRole getRole() {
        return role;
    }

    public CustomerEntity() {
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }
}
