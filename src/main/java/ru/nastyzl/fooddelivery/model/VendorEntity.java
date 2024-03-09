package ru.nastyzl.fooddelivery.model;

import ru.nastyzl.fooddelivery.enums.UserRole;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = UserRole.Values.VENDOR)
public class VendorEntity extends UserEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    public VendorEntity() {
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
}
