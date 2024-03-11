package ru.nastyzl.fooddelivery.service;

import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.repository.DishRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    public DishServiceImpl(DishRepository dishRepository, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
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
    public DishEntity save(DishDto dishDto) {
        DishEntity dishEntity = dishMapper.dishDtoToEntity(dishDto);
        return dishRepository.save(dishEntity);
    }

}
