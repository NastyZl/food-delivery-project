package ru.nastyzl.fooddelivery.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.nastyzl.fooddelivery.enums.UserRole;
import ru.nastyzl.fooddelivery.model.*;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImpl implements UserDetails {
    private final UserEntity user;

    public UserDetailsImpl(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(getRole(user)));
    }

    private String getRole(UserEntity user) {
        if (user instanceof CustomerEntity)
            return UserRole.CUSTOMER.getValue();
        if (user instanceof VendorEntity)
            return UserRole.VENDOR.getValue();
        if (user instanceof CourierEntity)
            return UserRole.COURIER.getValue();
        if (user instanceof AdminEntity)
            return UserRole.ADMIN.getValue();
        else throw new RuntimeException("Роль не определена");
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
