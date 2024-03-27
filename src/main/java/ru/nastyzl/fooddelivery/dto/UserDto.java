package ru.nastyzl.fooddelivery.dto;

import javax.validation.constraints.*;

public class UserDto {
    private Long id;
    @NotBlank(message = "Необходимо указать логин")
    private String username;
    @NotNull
    private String role;
    private boolean isLocked;

    @NotBlank(message = "Необходимо указать имя")
    private String firstName;

    @NotBlank(message = "Необходимо указать фамилию")
    private String lastName;

    @Pattern(regexp = "\\+7[0-9]{10}", message = "Телефонный номер должен начинаться с +7, затем - 10 цифр")
    private String phone;

    @Email(message = "Email должен быть корректным адресом электронной почты")
    @NotBlank(message = "Необходимо указать email")
    private String email;

    @NotBlank(message = "Необходимо указать пароль")
    private String password;

    @NotBlank(message = "Необходимо указать город")
    private String city;
    @NotBlank(message = "Необходимо указать улицу")
    private String streetName;

    @NotBlank(message = "Необходимо указать номер дома")
    private String streetNumber;

    @NotBlank(message = "Необходимо указать квартиру")
    private String apartment;
    @NotNull(message = "Необходимо указать этаж'")
    @Positive(message = "Отрицательным у нас не бывает")
    private Integer floor;

    @NotNull(message = "Необходимо указать подъезд'")
    @Positive(message = "Отрицательным у нас не бывает")
    private Integer entrance;

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getEntrance() {
        return entrance;
    }

    public void setEntrance(Integer entrance) {
        this.entrance = entrance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
