package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.OrderStatus;
import ru.nastyzl.fooddelivery.enums.PaymentType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

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
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(name = "order_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "cust_address", nullable = false)
    private String customerAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItemEntity> orderItems = new ArrayList<>();


    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public VendorEntity getVendor() {
        return vendor;
    }

    public void setVendor(VendorEntity vendor) {
        this.vendor = vendor;
    }

    public CourierEntity getCourier() {
        return courier;
    }

    public void setCourier(CourierEntity courier) {
        this.courier = courier;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

}
