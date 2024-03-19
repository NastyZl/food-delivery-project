package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DishService dishService;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, DishService dishService, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dishService = dishService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public CartEntity addItemToCart(DishShowDto dishDto, Integer quantity, String username) throws DishNotFoundException, DifferentVendorsException, UserNotFoundException {
        CustomerEntity user = userService.getCustomerByUsername(username);
        CartEntity cart = user.getCart();
        if (cart == null) {
            cart = new CartEntity();
        } else if (checkIfVendorOfAllDishesMatches(cart.getCartItems(), dishService.getVendorIdByDishId(dishDto.getId()))) {
            throw new DifferentVendorsException("разные вендоры");
        }

        Set<CartItemEntity> cartItemList = cart.getCartItems();

        CartItemEntity cartItem = find(cartItemList, dishDto.getId());

//        DishEntity dish = mapper.dishDtoToEntity(dishDto);
        DishEntity dish = dishService.dishShowDtoToDishEntity(dishDto);

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

        cart.setCustomer(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public CartEntity updateCart(DishShowDto dishShowDto, Integer quantity, String username) throws UserNotFoundException {
        Optional<? extends UserEntity> userOptional = userService.getByUsername(username);
        if (userOptional.isPresent()) {
            CustomerEntity customer = (CustomerEntity) userOptional.get();
            CartEntity cart = customer.getCart();
            Set<CartItemEntity> cartItemSet = cart.getCartItems();
            CartItemEntity item = find(cartItemSet, dishShowDto.getId());
            item.setQuantity(quantity);
            cart.setTotalPrise(totalPrice(cart.getCartItems()));
            cart.setTotalItems(totalItem(cart.getCartItems()));
            return cartRepository.save(cart);
        } else {
            throw new UserNotFoundException("update cart");
        }
    }

    @Override
    @Transactional
    public CartEntity removeItemFromCart(DishShowDto dishDto, String username) throws UserNotFoundException {
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
            throw new UserNotFoundException("update cart");
        }
    }

    @Override
    @Transactional
    public void deleteCartById(Long id) throws UserNotFoundException {
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
            throw new UserNotFoundException("Cart with id " + id + " not found");
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
    public CartEntity getCart(String username) throws UserNotFoundException {
        CartEntity cart = userService.getCustomerByUsername(username).getCart();
        return userService.getCustomerByUsername(username).getCart();
    }

    @Override
    public VendorEntity getVendorByCartID(Long id) throws UserNotFoundException {
        Optional<CartEntity> cart = cartRepository.findById(id);
        if (cart.isPresent()) {
            return cart.get().getCartItems()
                    .stream()
                    .map(CartItemEntity::getDish)
                    .map(DishEntity::getVendorEntity)
                    .findFirst()
                    .orElseThrow(() -> new UserNotFoundException("Вендор не найден для корзины"));
        }
        throw new UserNotFoundException("Корзина не найдена");
    }

    private boolean checkIfVendorOfAllDishesMatches(Set<CartItemEntity> cartItems, Long vendorId) {
        return cartItems.stream()
                .map(CartItemEntity::getDish)
                .anyMatch(dish -> !dish.getVendorEntity().getId().equals(vendorId));
    }

    @Transactional
    public void removeProductFromCarts(Long dishId) {
        List<CartEntity> allCart = cartRepository.findAll();
        for (CartEntity cart : allCart) {
            List<CartItemEntity> items = cartItemRepository.findByDish_IdAndCart_Id(dishId, cart.getId());
            if (!items.isEmpty()) {
                for (CartItemEntity item : items) {
                    cartItemRepository.delete(item);
                    cart.setTotalPrise(cart.getTotalPrise() - item.getUnitPrice());
                }
                cartRepository.save(cart);
            }
        }
    }


}
