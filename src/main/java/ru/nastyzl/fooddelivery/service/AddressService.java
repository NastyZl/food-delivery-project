package ru.nastyzl.fooddelivery.service;

import ru.nastyzl.fooddelivery.model.AddressEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;

public interface AddressService {
    AddressEntity save(AddressEntity addressEntity, VendorEntity vendor);
}
