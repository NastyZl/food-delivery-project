package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.mapper.AddressMapper;
import ru.nastyzl.fooddelivery.mapper.UserMapper;
import ru.nastyzl.fooddelivery.model.AddressEntity;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.AddressRepository;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository<UserEntity> userRepository;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository<UserEntity> userRepository, AddressRepository addressRepository, AddressMapper addressMapper, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public UserEntity registerUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        switch (userDto.getRole()) {
            case UserRole.Values.VENDOR:
                AddressEntity saveAddress = addressRepository.save(addressMapper.userDtoToAddressEntity(userDto));
                VendorEntity vendorEntity = userMapper.userDtoToVendorEntity(userDto);
                saveAddress.addVendor(vendorEntity);
                return userRepository.save(vendorEntity);
            case UserRole.Values.CUSTOMER:
                return userRepository.save(userMapper.userDtoToCustomerEntity(userDto));
            case UserRole.Values.COURIER:
                return userRepository.save(userMapper.userDtoToCourierEntity(userDto));
            default:
                throw new RuntimeException("Invalid role");
        }
    }

    @Override
    public Optional<? extends UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public List<DishEntity> getAllDishes(String username) {
        Optional<? extends UserEntity> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get() instanceof VendorEntity) {
            return ((VendorEntity) user.get()).getDishes();
        } else {
            return Collections.emptyList();
        }
    }
}
