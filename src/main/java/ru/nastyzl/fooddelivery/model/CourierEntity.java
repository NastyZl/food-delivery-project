package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = UserRole.Values.COURIER)
public class CourierEntity extends UserEntity {
    @Transient
    private final UserRole role = UserRole.COURIER;
    private Boolean availability;

    @OneToMany(mappedBy = "courier")
    private List<OrderEntity> orders = new ArrayList<>();

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void addOrder(OrderEntity order) {
        this.orders.add(order);
    }

    public void deleteOrder(Long id) {
        this.orders.removeIf(order -> order.getId().equals(id));
    }

    @Column(name = "chat_id")
    private Long chatId;

    public boolean isAvailable() {
        if (availability != null)
            return availability;
        else return false;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public UserRole getRole() {
        return role;
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
