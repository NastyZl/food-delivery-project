package ru.nastyzl.fooddelivery.dto;

import ru.nastyzl.fooddelivery.model.VendorEntity;

import javax.validation.constraints.*;

public class DishDto {
    @NotBlank(message = "Необходимо указать имя")
    private String dishName;
    @NotNull(message = "Необходимо указать цену")
    @Positive(message = "Цена должна быть положительна")
    private Double currentPrice;

    @NotBlank(message = "Необходимо указать описание")
    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    private String description;

    private VendorEntity vendorEntity;

    private String imgPath;

    @NotNull(message = "Необходимо указать количество")
    @PositiveOrZero(message = "Количество порций блюда не должно быть отрицательным числом")
    private Integer quantity;

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VendorEntity getVendorEntity() {
        return vendorEntity;
    }

    public void setVendorEntity(VendorEntity vendorEntity) {
        this.vendorEntity = vendorEntity;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
