package ru.nastyzl.fooddelivery.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cust_id")
    private CustomerEntity customer;

    @Column(name = "total_prise")
    private Double totalPrise;

    @Column(name = "total_items")
    private Integer totalItems;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "cart", fetch = FetchType.EAGER)
    private Set<CartItemEntity> cartItems = new HashSet<>();

    public CartEntity() {
        this.totalPrise = 0.0;
        this.totalItems = 0;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
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

    public Set<CartItemEntity> getCartItems() {
        return cartItems;
    }

    public void addCartItems(CartItemEntity cartItem) {
        this.cartItems.add(cartItem);
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
