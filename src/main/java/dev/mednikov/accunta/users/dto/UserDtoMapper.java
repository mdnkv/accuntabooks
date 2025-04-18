package dev.mednikov.accunta.users.dto;

import dev.mednikov.accunta.users.models.User;

import java.util.function.Function;

public final class UserDtoMapper implements Function<User, UserDto> {

    @Override
    public UserDto apply(User user) {
        UserDto result = new UserDto();
        result.setEmail(user.getEmail());
        result.setFirstName(user.getFirstName());
        result.setLastName(user.getLastName());
        result.setId(user.getId());
        return result;
    }

}
