package ru.nastyzl.fooddelivery.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nastyzl.fooddelivery.dto.UserDto;
import ru.nastyzl.fooddelivery.mapper.UserToEntityMapper;
import ru.nastyzl.fooddelivery.model.UserEntity;
import ru.nastyzl.fooddelivery.repository.UserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository<UserEntity> userRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserToEntityMapper mapper;

    public UserServiceImpl(UserRepository<UserEntity> userRepository, PasswordEncoder passwordEncoder, UserToEntityMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    public UserEntity registerUser(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        UserEntity user = mapper.userDtoToUserEntity(userDto);
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}
