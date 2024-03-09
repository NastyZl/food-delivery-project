package ru.nastyzl.fooddelivery.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;
import ru.nastyzl.fooddelivery.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository<UserEntity> userRepository;

    public UserDetailsServiceImpl(UserRepository<UserEntity> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User" + username + "not found");
        }
        return new UserDetailsImpl(user);
    }
}
