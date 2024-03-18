package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.mapper.VendorMapper;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.CartItemRepository;
import ru.nastyzl.fooddelivery.repository.DishRepository;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final UserService userService;
    private final DishMapper dishMapper;
    private final CartItemRepository cartItemRepository;

    public DishServiceImpl(DishRepository dishRepository, UserService userService, DishMapper dishMapper, CartItemRepository cartItemRepository) {
        this.dishRepository = dishRepository;
        this.userService = userService;
        this.dishMapper = dishMapper;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Optional<DishEntity> getByDishName(String dishName) {
        return dishRepository.findByDishName(dishName);
    }

    @Override
    public DishShowDto getById(Long id) {
        Optional<DishEntity> dishEntity = dishRepository.findById(id);
        if (dishEntity.isPresent()) {
            return dishEntityToDishShowDto(dishEntity.get());
        } else throw new RuntimeException("Ошибка при попытке получить блюдо по ID");
    }

    @Override
    public DishEntity update(DishCreateDto dishCreateDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cartItemRepository.deleteAllByDishId(id);
        dishRepository.deleteById(id);
    }

    @Override
    public List<DishCreateDto> findAll() {
        return dishRepository.findAll().stream()
                .map(dishMapper::dishEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DishEntity save(DishCreateDto dishCreateDto, String username) throws CustomerNotFoundException {
        VendorEntity vendor = userService.getVendorByUsername(username);
        DishEntity dishEntity = dishMapper.dishDtoToEntity(dishCreateDto);
        dishEntity.setVendorEntity(vendor);
        vendor.addDish(dishEntity);
        return dishRepository.save(dishEntity);
    }

    @Override
    public Long getVendorIdByDishId(Long id) throws DishNotFoundException {
        return dishRepository.findById(id)
                .map(dish -> dish.getVendorEntity().getId())
                .orElseThrow(DishNotFoundException::new);
    }

    @Override
    public List<DishShowDto> getAllDishesForVendor(Long vendorId) {
        return dishRepository.findDishesByVendorId(vendorId).stream()
                .map(this::dishEntityToDishShowDto).collect(Collectors.toList());
    }

    @Override
    public List<DishShowDto> getAllDish() {
        List<DishEntity> dishEntityList = dishRepository.findAll();
        return dishEntityList.stream()
                .map(this::dishEntityToDishShowDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishShowDto dishEntityToDishShowDto(DishEntity dishEntity) {
        DishShowDto dishDto = new DishShowDto();
        dishDto.setId(dishEntity.getId());
        dishDto.setDishName(dishEntity.getDishName());
        dishDto.setDescription(dishEntity.getDescription());
        dishDto.setCurrentPrice(dishEntity.getCurrentPrice());
        dishDto.setImgPath(dishEntity.getImgPath());
        dishDto.setQuantity(dishEntity.getQuantity());
        dishDto.setVendor(VendorMapper.INSTANCE.vendorEntityToVendorDto(dishEntity.getVendorEntity()));
        return dishDto;
    }

    @Override
    public DishEntity dishShowDtoToDishEntity(DishShowDto dishShowDto) {
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(dishShowDto.getId());
        dishEntity.setDishName(dishShowDto.getDishName());
        dishEntity.setDescription(dishShowDto.getDescription());
        dishEntity.setCurrentPrice(dishShowDto.getCurrentPrice());
        dishEntity.setImgPath(dishShowDto.getImgPath());
        dishEntity.setQuantity(dishShowDto.getQuantity());
        dishEntity.setVendorEntity(VendorMapper.INSTANCE.vendorDtoToVendorEntity(dishShowDto.getVendor()));
        return dishEntity;
    }


}
