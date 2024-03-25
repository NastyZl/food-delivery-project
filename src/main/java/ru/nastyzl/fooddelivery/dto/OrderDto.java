package ru.nastyzl.fooddelivery.dto;

import ru.nastyzl.fooddelivery.enums.PaymentType;
import ru.nastyzl.fooddelivery.model.CartEntity;

import javax.validation.constraints.NotBlank;

public class OrderDto {
    @NotBlank(message = "Заполните адрес")
    String address;

    PaymentType paymentType;

    CartEntity cart;

    public OrderDto() {
    }

    public OrderDto(CartEntity cart) {
        this.cart = cart;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }
}
