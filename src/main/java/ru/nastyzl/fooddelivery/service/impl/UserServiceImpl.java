package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Contact;
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

import java.util.*;
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
     * Activates courier by phone number
     *
     * @param contact user's contact information from telegram
     */
    @Override
    public void activateCourier(Contact contact) {
        Optional<CourierEntity> optionalUser = userRepository.findCourierByPhoneNumber(contact.getPhoneNumber());
        if (optionalUser.isPresent()) {
            CourierEntity user = optionalUser.get();
            user.setChatId(contact.getUserId());
            user.setAvailability(true);
            userRepository.save(user);
        }
    }



    @Override
    public Optional<CourierEntity> findCourierByChatId(Long id) {
        return userRepository.findCourierByChatId(id);
    }

    @Override
    public Optional<CourierEntity> findCourierByPhoneNumber(String phone) {
        return userRepository.findCourierByPhoneNumber(phone);
    }

    @Override
    @Transactional
    public Optional<? extends UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean checkAvailable(Long id) {
        CourierEntity courierEntity = userRepository.findAllCourier().stream().filter(courier -> Objects.equals(courier.getId(), id)).findFirst().get();
        if (courierEntity.getOrderEntityList().isEmpty()) {
            return true;
        }
        return false;
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
    @Transactional
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
        CourierEntity courierEntity = couriers.get(random.nextInt(couriers.size()));
        courierEntity.setAvailability(false);
        return courierEntity;
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
