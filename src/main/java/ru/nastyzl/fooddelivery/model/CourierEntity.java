package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue(value = UserRole.Values.COURIER)
public class CourierEntity extends UserEntity {
    @Transient
    private final UserRole role = UserRole.COURIER;
    private Boolean availability;

    @Column(name = "chat_id")
    private Long chatId;

    public boolean isAvailable() {
        if (availability!=null)
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
