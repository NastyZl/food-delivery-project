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

    @OneToMany
    private List<OrderEntity> orderEntityList = new ArrayList<>();

    public List<OrderEntity> getOrderEntityList() {
        return orderEntityList;
    }

    public void addOrderEntity(OrderEntity order) {
        this.orderEntityList.add(order);
    }

    public void deleteOrderEntity(Long id) {
        this.orderEntityList.removeIf(order -> order.getId().equals(id));
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
