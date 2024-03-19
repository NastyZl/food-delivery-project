package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.InvalidRoleException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.mapper.AddressMapper;
import ru.nastyzl.fooddelivery.mapper.UserMapper;
import ru.nastyzl.fooddelivery.model.*;
import ru.nastyzl.fooddelivery.repository.AddressRepository;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

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
    public VendorEntity getVendorByUsername(String username) throws UserNotFoundException {
        return userRepository.findAllVendor().stream().filter(vendor -> vendor.getUsername().equals(username)).findFirst().orElseThrow(() -> new UserNotFoundException("вендор не найден"));
    }

    @Override
    public CustomerEntity getCustomerByUsername(String username) throws UserNotFoundException {
        return userRepository.findAllCustomer().stream().filter(vendor -> vendor.getUsername().equals(username)).findFirst().orElseThrow(() -> new UserNotFoundException("пользователь не найден"));
    }

    @Override
    public Optional<CourierEntity> getCourierByUsername(String username) {
        return userRepository.findAllCourier().stream().filter(courier -> courier.getUsername().equals(username)).findFirst();
    }

    /**
     * Register new user depending on the role
     *
     * @param userDto user data transfer
     * @return registration user
     * @throws InvalidRoleException if an unknown role is passed
     */
    @Override
    public UserEntity registerUser(UserDto userDto) throws InvalidRoleException {
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
                throw new InvalidRoleException();
        }
    }

    /**
     * Activates courier by username and setting availability to 'false'
     *
     * @param username username of courier
     * @param chatId ID of telegram chat
     * @return 'true' if courier activated
     *          'false' if this courier has already activated telegram bot
     */
    @Override
    public boolean activateCourier(String username, Long chatId) {
        Optional<CourierEntity> user = this.getCourierByUsername(username);
        if (user.isPresent()) {
            user.get().setChatId(chatId);
            user.get().setAvailability(false);
            userRepository.save(user.get());
            return true;
        }
        return false;
    }

    @Override
    public Optional<CourierEntity> findByChatId(Long id) {
        return userRepository.findByChatId(id);
    }

    @Override
    @Transactional
    public Optional<? extends UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Choose courier for order.
     * First, free activated couriers are checked and, if available, selected randomly.
     * If not found, then select a randomly busy activated courier.
     *
     * @return entity of chosen courier
     * @throws UserNotFoundException if courier not found
     */
    @Override
    public CourierEntity chooseCourier() throws UserNotFoundException {

        List<CourierEntity> busyCouriers = userRepository.findAllCourier()
                .stream()
                .filter(courier -> !courier.isAvailable() && courier.getChatId() != null)
                .collect(Collectors.toList());

        List<CourierEntity> availableCouriers = userRepository.findAllCourier()
                .stream()
                .filter(courier -> courier.isAvailable() && courier.getChatId() != null)
                .collect(Collectors.toList());

        if (!availableCouriers.isEmpty()) {
            return getRandomCourier(availableCouriers);
        } else if (!busyCouriers.isEmpty()) {
            return getRandomCourier(busyCouriers);
        } else
            throw new UserNotFoundException("Ни один курьер не найден.");
    }

    private CourierEntity getRandomCourier(List<CourierEntity> couriers) {
        Random random = new Random();
        return couriers.get(random.nextInt(couriers.size()));
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
