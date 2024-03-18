package ru.nastyzl.fooddelivery.dto;

import javax.validation.constraints.*;

public class DishCreateDto {
    @NotBlank(message = "Необходимо указать имя")
    private String dishName;
    @NotNull(message = "Необходимо указать цену")
    @Positive(message = "Цена должна быть положительна")
    private Double currentPrice;

    @NotBlank(message = "Необходимо указать описание")
    @Size(min = 10, message = "Описание должно быть не менее 10 символов")
    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    private String description;

    private VendorDto vendor;

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


    public VendorDto getVendor() {
        return vendor;
    }

    public void setVendor(VendorDto vendor) {
        this.vendor = vendor;
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
