package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import ru.nastyzl.fooddelivery.dto.DishDto;
import ru.nastyzl.fooddelivery.exception.CustomerNotFoundException;
import ru.nastyzl.fooddelivery.exception.DifferentVendorsException;
import ru.nastyzl.fooddelivery.exception.DishNotFoundException;
import ru.nastyzl.fooddelivery.mapper.DishMapper;
import ru.nastyzl.fooddelivery.model.*;
import ru.nastyzl.fooddelivery.repository.CartItemRepository;
import ru.nastyzl.fooddelivery.repository.CartRepository;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.service.CartService;
import ru.nastyzl.fooddelivery.service.DishService;
import ru.nastyzl.fooddelivery.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository<CustomerEntity> customerRepository;
    private final DishService dishService;
    private final UserService userService;
    private final DishMapper mapper;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository,
                           UserRepository<CustomerEntity> customerRepository, DishService dishService,
                           UserService userService, DishMapper mapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.dishService = dishService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CartEntity addItemToCart(DishDto dishDto, Integer quantity, String username) throws DishNotFoundException, DifferentVendorsException {
        CartEntity cart = getCart(username);

        if (cart == null) {
            cart = new CartEntity();
        } else if (checkIfVendorOfAllDishesMatches(cart.getCartItems(), dishService.getVendorIdByDishId(dishDto.getId()))) {
            throw new DifferentVendorsException("разные вендоры");
        }

        Set<CartItemEntity> cartItemList = cart.getCartItems();


        CartItemEntity cartItem = find(cartItemList, dishDto.getId());

        DishEntity dish = mapper.dishDtoToEntity(dishDto);


        if (cartItem == null) {
            cartItem = new CartItemEntity();
            cartItem.setDish(dish);
            cartItem.setUnitPrice(dish.getCurrentPrice());
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        }

        cart.addCartItems(cartItem);
        cart.setTotalPrise(totalPrice(cart.getCartItems()));
        cart.setTotalItems(totalItem(cart.getCartItems()));
        cart.setCustomer(customerRepository.findByUsername(username).get());

        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartEntity updateCart(DishDto dishDto, Integer quantity, String username) throws CustomerNotFoundException {
        Optional<? extends UserEntity> userOptional = userService.getByUsername(username);
        if (userOptional.isPresent()) {
            CustomerEntity customer = (CustomerEntity) userOptional.get();
            CartEntity cart = customer.getCart();
            Set<CartItemEntity> cartItemSet = cart.getCartItems();
            CartItemEntity item = find(cartItemSet, dishDto.getId());
            item.setQuantity(quantity);
            cart.setTotalPrise(totalPrice(cart.getCartItems()));
            cart.setTotalItems(totalItem(cart.getCartItems()));
            return cartRepository.save(cart);
        } else {
            throw new CustomerNotFoundException("update cart");
        }
    }

    @Override
    @Transactional
    public CartEntity removeItemFromCart(DishDto dishDto, String username) throws CustomerNotFoundException {
        Optional<? extends UserEntity> userOptional = userService.getByUsername(username);
        if (userOptional.isPresent()) {
            CustomerEntity customer = (CustomerEntity) userOptional.get();
            CartEntity cart = customer.getCart();
            Set<CartItemEntity> cartItemSet = cart.getCartItems();
            CartItemEntity item = find(cartItemSet, dishDto.getId());
            cartItemSet.remove(item);
            cartItemRepository.delete(item);
            cart.setTotalPrise(totalPrice(cart.getCartItems()));
            cart.setTotalItems(totalItem(cart.getCartItems()));
            return cartRepository.save(cart);
        } else {
            throw new CustomerNotFoundException("update cart");
        }
    }

    @Override
    @Transactional
    public void deleteCartById(Long id) throws CustomerNotFoundException {
        Optional<CartEntity> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            if (!ObjectUtils.isEmpty(cart.get().getCartItems())) {
                cartItemRepository.deleteAll(cart.get().getCartItems());
            }

            cart.get().getCartItems().clear();
            cart.get().setTotalPrise(0d);
            cart.get().setTotalItems(0);
            cartRepository.save(cart.get());
        } else {
            throw new CustomerNotFoundException("Cart with id " + id + " not found");
        }
    }

    private CartItemEntity find(Set<CartItemEntity> cartItemEntities, Long dishId) {
        return cartItemEntities.stream()
                .filter(item -> item.getDish().getId().equals(dishId))
                .findFirst().orElse(null);
    }

    private Integer totalItem(Set<CartItemEntity> cartItemSet) {
        return cartItemSet.stream()
                .mapToInt(CartItemEntity::getQuantity)
                .sum();
    }

    private Double totalPrice(Set<CartItemEntity> cartItemSet) {
        return cartItemSet.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public CartEntity getCart(String username) {
        return customerRepository.findByUsername(username).map(CustomerEntity::getCart).orElse(null);
    }

    private boolean checkIfVendorOfAllDishesMatches(Set<CartItemEntity> cartItems, Long vendorId) {
        return cartItems.stream()
                .map(CartItemEntity::getDish)
                .anyMatch(dish -> !dish.getVendorEntity().getId().equals(vendorId));
    }

}
