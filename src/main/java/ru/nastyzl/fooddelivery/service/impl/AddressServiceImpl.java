package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.mapper.AddressMapper;
import ru.nastyzl.fooddelivery.model.AddressEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.AddressRepository;
import ru.nastyzl.fooddelivery.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
    }

    @Override
    public AddressEntity save(AddressEntity addressEntity, VendorEntity vendorEntity) {
        addressEntity.addVendor(vendorEntity);
        return addressRepository.save(addressEntity);
    }
}
