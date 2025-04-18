package dev.mednikov.accunta.users.services;

import dev.mednikov.accunta.users.dto.CreateUserRequestDto;
import dev.mednikov.accunta.users.dto.UserDto;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserDto createUser (CreateUserRequestDto payload);

    UserDto updateUser (UserDto payload);

    Optional<UserDto> getUser (UUID id);

    void deleteUser (UUID id);

}
