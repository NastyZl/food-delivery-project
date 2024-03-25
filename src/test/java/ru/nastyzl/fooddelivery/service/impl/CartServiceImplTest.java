package ru.nastyzl.fooddelivery.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nastyzl.fooddelivery.dto.DishShowDto;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.exception.UserNotFoundException;
import ru.nastyzl.fooddelivery.model.*;
import ru.nastyzl.fooddelivery.repository.CartItemRepository;
import ru.nastyzl.fooddelivery.repository.CartRepository;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CartServiceImplTest {
    @MockBean
    UserService userService;

    @MockBean
    DishService dishService;

    @MockBean
    CartRepository cartRepository;

    @MockBean
    CartItemRepository cartItemRepository;

    @Autowired
    CartService cartService;

    private CustomerEntity customer;
    private CartEntity cart;
    private CartItemEntity cartItem;
    private Set<CartItemEntity> cartItems;
    private DishEntity dishEntity;
    private DishShowDto dishShowDto;
    private String username;
    private VendorEntity vendor;

    @BeforeEach
    private void setUp() {
        username = "user";

        vendor = new VendorEntity();
        vendor.setId(1L);
        dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setQuantity(5);
        dishEntity.setVendorEntity(vendor);

        dishShowDto = new DishShowDto();
        dishShowDto.setId(1L);

        cartItem = new CartItemEntity();
        cartItem.setQuantity(1);
        cartItem.setUnitPrice(50D);
        cartItem.setDish(dishEntity);

        cartItems = new HashSet<>();
        cartItems.add(cartItem);

        cart = new CartEntity();
        cart.setId(1L);
        cart.setCartItems(cartItems);
        cart.setCustomer(customer);

        customer = new CustomerEntity();
        customer.setCart(cart);
        customer.setUsername(username);
    }

    @Test
    public void testRemoveItemFromCart() {
        Optional<? extends UserEntity> optionalUser = Optional.of(customer);
        Mockito.<Optional<? extends UserEntity>>when(userService.getByUsername(username)).thenReturn(optionalUser);

        when(dishService.findById(1L)).thenReturn(Optional.of(dishEntity));

        assertDoesNotThrow(() -> cartService.removeItemFromCart(dishShowDto, username));

        verify(userService, times(1)).getByUsername(username);
        verify(dishService, times(1)).findById(1L);
        verify(cartItemRepository, times(1)).delete(any());
        verify(cartRepository, times(1)).save(any());
    }

    @Test
    @Transactional
    public void testAddItemToCart() throws DishNotFoundException, UserNotFoundException, DifferentVendorsException {

        when(userService.getCustomerByUsername(username)).thenReturn(customer);
        when(dishService.getVendorIdByDishId(dishShowDto.getId())).thenReturn(vendor.getId());
        when(dishService.dishShowDtoToDishEntity(dishShowDto)).thenReturn(dishEntity);
        when(dishService.findById(1L)).thenReturn(Optional.of(dishEntity));
        when(cartRepository.save(cart)).thenReturn(cart);

        cart = cartService.addItemToCart(dishShowDto, 1, username);

        assertNotNull(cart);
        assertEquals(1, cart.getCartItems().size());
        assertEquals(100D, cart.getTotalPrise());
        assertEquals(2, cart.getTotalItems());

        verify(userService, times(1)).getCustomerByUsername(username);
        verify(dishService, times(1)).dishShowDtoToDishEntity(dishShowDto);
        verify(dishService, times(1)).decreaseDishQuantityById(1L);
        verify(cartItemRepository, times(1)).save(any());
        verify(cartRepository, times(1)).save(any());
    }
}