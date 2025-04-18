package dev.mednikov.accunta.users.services;

import dev.mednikov.accunta.users.dto.CreateUserRequestDto;
import dev.mednikov.accunta.users.dto.UserDto;
import dev.mednikov.accunta.users.dto.UserDtoMapper;
import dev.mednikov.accunta.users.exceptions.UserAlreadyExistsException;
import dev.mednikov.accunta.users.exceptions.UserNotFoundException;
import dev.mednikov.accunta.users.models.User;
import dev.mednikov.accunta.users.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final static UserDtoMapper userDtoMapper = new UserDtoMapper();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(CreateUserRequestDto payload) {
        if (this.userRepository.findByEmail(payload.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setEmail(payload.getEmail());
        user.setPassword(this.passwordEncoder.encode(payload.getPassword()));
        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());

        User result = this.userRepository.save(user);
        return userDtoMapper.apply(result);
    }

    @Override
    public UserDto updateUser(UserDto payload) {
        Objects.requireNonNull(payload.getId());
        User user = userRepository.findById(payload.getId()).orElseThrow(UserNotFoundException::new);

        user.setFirstName(payload.getFirstName());
        user.setLastName(payload.getLastName());

        if (!payload.getEmail().equals(user.getEmail())) {
            if (this.userRepository.findByEmail(payload.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException();
            }
            user.setEmail(payload.getEmail());
        }

        User result = this.userRepository.save(user);
        return userDtoMapper.apply(result);
    }

    @Override
    public Optional<UserDto> getUser(UUID id) {
        return this.userRepository.findById(id).map(userDtoMapper);
    }

    @Override
    public void deleteUser(UUID id) {
        this.userRepository.deleteById(id);
    }

}
