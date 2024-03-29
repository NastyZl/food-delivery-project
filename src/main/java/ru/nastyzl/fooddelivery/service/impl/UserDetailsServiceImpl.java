package ru.nastyzl.fooddelivery.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;

import java.util.Optional;

/**
 * Implementation of UserDetailsService interface
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository<UserEntity> userRepository;

    public UserDetailsServiceImpl(UserRepository<UserEntity> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<? extends UserEntity> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));
        return user.map(UserDetailsImpl::new).get();
    }
}
