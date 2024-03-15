package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.enums.PaymentType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cust_id", referencedColumnName = "id", nullable = false)
    private CustomerEntity customer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    private VendorEntity vendor;

    @ManyToOne(optional = false)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false)
    private CourierEntity courier;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType;


    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "cust_address", nullable = false)
    private String customerAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
