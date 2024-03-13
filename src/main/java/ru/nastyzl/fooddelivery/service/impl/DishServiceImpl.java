package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.DishRepository;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.service.DishService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final UserRepository<VendorEntity> vendorRepository;
    private final DishMapper dishMapper;

    public DishServiceImpl(DishRepository dishRepository, UserRepository<VendorEntity> vendorRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.vendorRepository = vendorRepository;
        this.dishMapper = dishMapper;
    }

    @Override
    public Optional<DishEntity> getByDishName(String dishName) {
        return dishRepository.findByDishName(dishName);
    }


    @Override
    public DishEntity update(DishDto dishDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        dishRepository.deleteById(id);
    }

    @Override
    public List<DishDto> findAll() {
        return dishRepository.findAll().stream()
                .map(dishMapper::dishEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DishEntity save(DishDto dishDto, Long vendorId) {
        Optional<VendorEntity> vendor = vendorRepository.findById(vendorId);
        if (vendor.isPresent()) {
            DishEntity dishEntity = dishMapper.dishDtoToEntity(dishDto);
            dishEntity.setVendorEntity(vendor.get());
            vendor.get().addDish(dishEntity);
            return dishRepository.save(dishEntity);
        } else {
            throw new RuntimeException("Ошибка при сохранении нового блюда");
        }

    }

}
