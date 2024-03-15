package ru.nastyzl.fooddelivery.dto;

import ru.nastyzl.fooddelivery.model.CartItemEntity;
import ru.nastyzl.fooddelivery.model.CustomerEntity;

import java.util.Set;

public class CartDto {

    private Long id;

    private CustomerEntity customer;

    private Double totalPrise;

    private Integer totalItems;

    private Set<CartItemEntity> cartItems;

    public CartDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Double getTotalPrise() {
        return totalPrise;
    }

    public void setTotalPrise(Double totalPrise) {
        this.totalPrise = totalPrise;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Set<CartItemEntity> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemEntity> cartItems) {
        this.cartItems = cartItems;
    }
}
