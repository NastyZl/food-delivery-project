package ru.nastyzl.fooddelivery.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class DishDto {
    private Long id;
    @NotBlank(message = "Необходимо указать имя")
    private String dishName;
    @NotNull(message = "Необходимо указать цену")
    @Positive(message = "Цена должна быть положительна")
    private Double currentPrice;

    @NotBlank(message = "Необходимо указать описание")
    @Size(min = 10, message = "Описание должно быть не менее 10 символов")
    @Size(max = 255, message = "Описание не должно превышать 255 символов")
    private String description;

    private UserDto userDto;

    private MultipartFile imgPath;

    @NotNull(message = "Необходимо указать количество")
    @PositiveOrZero(message = "Количество порций блюда не должно быть отрицательным числом")
    private Integer quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public MultipartFile getImgPath() {
        return imgPath;
    }

    public void setImgPath(MultipartFile imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
