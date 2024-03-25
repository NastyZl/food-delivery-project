package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.NullQuantityOfDishesException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.mapper.VendorMapper;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.DishRepository;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for handling dish actions.
 */
@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final UserService userService;
    private final DishMapper dishMapper;

    public DishServiceImpl(DishRepository dishRepository, UserService userService, DishMapper dishMapper) {
        this.dishRepository = dishRepository;
        this.userService = userService;
        this.dishMapper = dishMapper;
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

    /**
     * Change delete flag of a dish by its ID. If the dish is not deleted, sets its quantity to 0.
     * If the dish's quantity is 0 and the delete flag is being set to false, throws an exception.
     * Delete all cart items associated with dish.
     *
     * @param id ID of dish.
     * @throws NullQuantityOfDishesException if the dish quantity is 0, and you are trying to activate the dish.
     */
    @Override
    @Transactional
    public void changeDeleteFlagById(Long id) throws NullQuantityOfDishesException {
        Optional<DishEntity> optionalDish = dishRepository.findById(id);
        if (optionalDish.isPresent()) {
            DishEntity dish = optionalDish.get();
            if (!dish.isDeleted()) {
                dish.setQuantity(0);
            } else if (dish.getQuantity() == 0) {
                throw new NullQuantityOfDishesException("Для того, чтобы восстановить блюдо в продажу, нужно сначала пополнить запасы.");
            }
            dish.setDeleted(!dish.isDeleted());
            dishRepository.save(dish);
        }
    }

    @Override
    public Page<DishShowDto> pageDishes(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        Page<DishEntity> dishEntityPage = dishRepository.pageDishes(pageable);
        return dishEntityPage.map(this::dishEntityToDishShowDto);
    }

    @Override
    public Optional<DishEntity> findById(Long id) {
        return dishRepository.findById(id);
    }

    @Override
    public Page<DishShowDto> searchDishes(String keyword, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);
        Page<DishEntity> dishes = dishRepository.searchDishes(keyword, pageable);
        return dishes.map(this::dishEntityToDishShowDto);
    }

    /**
     * Saves a new dish for a vendor.
     *
     * @param dishCreateDto the data transfer object containing the details of the new dish.
     * @param username      username of Vendor.
     * @return saved dish.
     * @throws UserNotFoundException if the vendor with the username is not found.
     */
    @Override
    @Transactional
    public DishEntity save(DishCreateDto dishCreateDto, String username) throws UserNotFoundException {
        VendorEntity vendor = userService.getVendorByUsername(username);
        DishEntity dishEntity = dishMapper.dishDtoToEntity(dishCreateDto);
        dishEntity.setVendorEntity(vendor);
        dishEntity.setDeleted(false);
        vendor.addDish(dishEntity);
        return dishRepository.save(dishEntity);
    }

    /**
     * Gets the ID of the vendor associated with a specific dish.
     *
     * @param id dish ID
     * @return vendor ID
     * @throws DishNotFoundException if the dish with the specified ID is not found.
     */
    @Override
    public Long getVendorIdByDishId(Long id) throws DishNotFoundException {
        return dishRepository.findById(id)
                .map(dish -> dish.getVendorEntity().getId())
                .orElseThrow(DishNotFoundException::new);
    }

    /**
     * Gets all dishes for a specific vendor.
     *
     * @param vendorId vendor ID.
     * @return a list of DishShowDto objects representing the dishes for the vendor.
     */
    @Override
    public List<DishShowDto> getAllDishesForVendor(Long vendorId) {
        return dishRepository.findDishesByVendorId(vendorId).stream()
                .map(this::dishEntityToDishShowDto).collect(Collectors.toList());
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
        dishDto.setDeleted(dishEntity.isDeleted());
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
        dishEntity.setDeleted(dishShowDto.isDeleted());
        dishEntity.setVendorEntity(VendorMapper.INSTANCE.vendorDtoToVendorEntity(dishShowDto.getVendor()));
        return dishEntity;
    }


}
