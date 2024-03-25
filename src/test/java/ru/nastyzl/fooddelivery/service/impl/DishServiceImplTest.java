package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nastyzl.fooddelivery.dto.DishCreateDto;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.dto.VendorDto;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.NullQuantityOfDishesException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.mapper.VendorMapper;
import ru.nastyzl.fooddelivery.model.DishEntity;
import ru.nastyzl.fooddelivery.model.VendorEntity;
import ru.nastyzl.fooddelivery.repository.DishRepository;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DishServiceImplTest {

    @Autowired
    DishServiceImpl dishService;

    @MockBean
    VendorMapper vendorMapper;

    @MockBean
    DishRepository dishRepository;

    @MockBean
    UserService userService;

    @MockBean
    DishMapper dishMapper;

    @Test
    public void testGetByDishName_DishFound() {
        DishEntity expectedDish = new DishEntity();
        expectedDish.setDishName("testName");

        when(dishRepository.findByDishName("testName")).thenReturn(Optional.of(expectedDish));

        Optional<DishEntity> actualDish = dishService.getByDishName("testName");

        assertEquals(expectedDish, actualDish.get());
    }

    @Test
    public void testGetByDishName_DishNotFound() {
        when(dishRepository.findByDishName("test")).thenReturn(Optional.empty());
        Optional<DishEntity> actualDish = dishService.getByDishName("test");

        assertEquals(Optional.empty(), actualDish);
    }

    @Test
    public void testChangeDeleteFlagById() throws NullQuantityOfDishesException {
        DishEntity dish = new DishEntity();
        dish.setId(1L);
        dish.setDeleted(false);
        dish.setQuantity(10);
        Optional<DishEntity> optionalDish = Optional.of(dish);

        when(dishRepository.findById(1L)).thenReturn(optionalDish);

        dishService.changeDeleteFlagById(1L);

        assertTrue(dish.isDeleted());
        assertEquals(0, dish.getQuantity());
        verify(dishRepository, times(1)).save(dish);
    }

    @Test
    public void testChangeDeleteFlagByIdWithNullQuantity() {

        DishEntity dish = new DishEntity();
        dish.setId(1L);
        dish.setDeleted(true);
        dish.setQuantity(0);
        Optional<DishEntity> optionalDish = Optional.of(dish);

        when(dishRepository.findById(1L)).thenReturn(optionalDish);

        assertThrows(NullQuantityOfDishesException.class, () -> dishService.changeDeleteFlagById(1L));
        verify(dishRepository, never()).save(dish);
    }

    @Test
    public void testGetAllDishesForVendor() {
        Long vendorId = 1L;
        DishEntity dishOne = new DishEntity();
        DishEntity dishTwo = new DishEntity();
        List<DishEntity> dishes = Arrays.asList(dishOne, dishTwo);

        when(dishRepository.findDishesByVendorId(any(Long.class))).thenReturn(dishes);

        List<DishShowDto> allDishesForVendor = dishService.getAllDishesForVendor(vendorId);

        assertEquals(2, allDishesForVendor.size());

        verify(dishRepository).findDishesByVendorId(vendorId);
    }

    @Test
    public void testGetVendorIdByDishId() throws DishNotFoundException {
        DishEntity dish = new DishEntity();
        dish.setId(1L);

        VendorEntity vendor = new VendorEntity();
        vendor.setId(2L);
        dish.setVendorEntity(vendor);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Long vendorId = dishService.getVendorIdByDishId(1L);

        verify(dishRepository, times(1)).findById(1L);
        assertEquals(2L, vendorId);
    }

    @Test
    public void testSave() throws UserNotFoundException {
        String username = "test";
        VendorEntity vendor = new VendorEntity();
        when(userService.getVendorByUsername(username)).thenReturn(vendor);

        DishEntity dishEntity = new DishEntity();
        DishCreateDto dishCreateDto = new DishCreateDto();

        when(dishMapper.dishDtoToEntity(dishCreateDto)).thenReturn(dishEntity);
        when(dishRepository.save(any(DishEntity.class))).thenReturn(dishEntity);

        DishEntity savedDish = dishService.save(dishCreateDto, username);

        assertEquals(vendor, savedDish.getVendorEntity());
        assertFalse(savedDish.isDeleted());
        verify(userService, times(1)).getVendorByUsername(username);
        verify(dishMapper, times(1)).dishDtoToEntity(dishCreateDto);
        verify(dishRepository, times(1)).save(any(DishEntity.class));
    }

    @Test
    public void testDishShowDtoToDishEntity() {

        DishShowDto dishShowDto = new DishShowDto();
        dishShowDto.setId(1L);
        dishShowDto.setDishName("test name");
        dishShowDto.setDescription("test description");
        dishShowDto.setCurrentPrice(100D);
        dishShowDto.setImgPath("/img/test.jpg");
        dishShowDto.setQuantity(10);
        dishShowDto.setDeleted(false);

        VendorDto vendorDto = new VendorDto();
        vendorDto.setId(1L);
        vendorDto.setUsername("test name");

        dishShowDto.setVendor(vendorDto);

        DishEntity dishEntity = dishService.dishShowDtoToDishEntity(dishShowDto);

        assertEquals(dishShowDto.getId(), dishEntity.getId());
        assertEquals(dishShowDto.getDishName(), dishEntity.getDishName());
        assertEquals(dishShowDto.getDescription(), dishEntity.getDescription());
        assertEquals(dishShowDto.getCurrentPrice(), dishEntity.getCurrentPrice());
        assertEquals(dishShowDto.getImgPath(), dishEntity.getImgPath());
        assertEquals(dishShowDto.getQuantity(), dishEntity.getQuantity());
        assertEquals(dishShowDto.isDeleted(), dishEntity.isDeleted());
        assertEquals(vendorDto.getId(), dishEntity.getVendorEntity().getId());
        assertEquals(vendorDto.getUsername(), dishEntity.getVendorEntity().getUsername());
    }

    @Test
    public void testDishEntityToDishShowDto() {
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setDishName("test name");
        dishEntity.setDescription("test description");
        dishEntity.setCurrentPrice(100D);
        dishEntity.setImgPath("/img/test.jpg");
        dishEntity.setQuantity(10);
        dishEntity.setDeleted(false);

        VendorEntity vendorEntity = new VendorEntity();
        dishEntity.setVendorEntity(vendorEntity);

        DishShowDto dishShowDto = dishService.dishEntityToDishShowDto(dishEntity);

        assertEquals(dishEntity.getId(), dishShowDto.getId());
        assertEquals(dishEntity.getDishName(), dishShowDto.getDishName());
        assertEquals(dishEntity.getVendorEntity().getUsername(), dishShowDto.getVendor().getUsername());
    }
}