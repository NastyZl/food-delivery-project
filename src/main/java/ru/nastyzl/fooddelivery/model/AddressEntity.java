package ru.nastyzl.fooddelivery.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(name = "street_name", nullable = false)
    private String streetName;

    @Column(name = "street_number", nullable = false)
    private String streetNumber;

    @Column(nullable = false)
    private String apartment;

    @Column(nullable = false, length = 10)
    private Integer floor;

    @Column(nullable = false)
    private Integer entrance;

    @OneToOne(mappedBy = "address")
    private VendorEntity vendor;

    public AddressEntity() {
    }

    @Override
    public String toString() {
        return city + ", " + streetName + ",  "
                + streetNumber + ", кв." + apartment +
                ", этаж " + floor +
                ", подъезд " + entrance;
    }

    public void addVendor(VendorEntity vendor) {
        vendor.setAddress(this);
        this.vendor = vendor;
    }

    public void removeVendor() {
        if (vendor != null) {
            vendor.setAddress(null);
            this.vendor = null;
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public VendorEntity getVendor() {
        return vendor;
    }

    public void setVendor(VendorEntity vendor) {
        this.vendor = vendor;
    }

}
