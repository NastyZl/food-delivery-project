package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nastyzl.fooddelivery.model.AddressEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.AddressRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AddressServiceImplTest {

    @Autowired
    AddressServiceImpl addressService;

    @MockBean
    AddressRepository addressRepository;

    @Test
    void save() {
        AddressEntity address = new AddressEntity();
        VendorEntity vendor = new VendorEntity();

        when(addressRepository.save(address)).thenReturn(address);

        AddressEntity savedAddress = addressService.save(address, vendor);

        assertEquals(address, savedAddress);
        assertEquals(vendor, savedAddress.getVendor());
    }
}