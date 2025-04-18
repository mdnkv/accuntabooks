package dev.mednikov.accunta.users.services;

import dev.mednikov.accunta.users.dto.CreateUserRequestDto;
import dev.mednikov.accunta.users.dto.UserDto;
import dev.mednikov.accunta.users.exceptions.UserAlreadyExistsException;
import dev.mednikov.accunta.users.exceptions.UserNotFoundException;
import dev.mednikov.accunta.users.models.User;
import dev.mednikov.accunta.users.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private UserServiceImpl userService;

    @Test
    void createUser_alreadyExistsText () {
        String email = "d7zrq60e1ufx7ppuo3c@comcast.net";

        User user = new User();
        user.setEmail(email);

        CreateUserRequestDto payload = new CreateUserRequestDto();
        payload.setFirstName("Fabiana Guadalupe");
        payload.setLastName("Montenegro Luján");
        payload.setEmail(email);
        payload.setPassword("password");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        Assertions.assertThatThrownBy(() -> userService.createUser(payload)).isInstanceOf(UserAlreadyExistsException.class);

    }

    @Test
    void createUser_successTest(){
        String email = "84gb1fcvew2u@msn.com";
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setEmail(email);
        user.setFirstName("Guadalupe María Fernanda");
        user.setLastName("Ozuna Burgos Hijo");
        user.setId(userId);

        CreateUserRequestDto payload = new CreateUserRequestDto();
        payload.setFirstName("Guadalupe María Fernanda");
        payload.setLastName("Ozuna Burgos Hijo");
        payload.setEmail(email);
        payload.setPassword("password");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(payload.getPassword())).thenReturn("password");
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.createUser(payload);
        Assertions.assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", userId);

    }

    @Test
    void updateUser_notFoundTest(){
        UUID userId = UUID.randomUUID();

        UserDto payload = new UserDto();
        payload.setId(userId);
        payload.setFirstName("Ana Sofía");
        payload.setLastName("Granados Hijo");
        payload.setEmail("0rmt7b423g2k7dh6h7@msn.com");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userService.updateUser(payload)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateUser_emailAlreadyExistsTest(){
        UUID userId = UUID.randomUUID();
        String email = "oftg1cp3q73q76wf3@msn.com";

        User user = new User();
        user.setId(userId);
        user.setEmail("niraj.gomez@googlemail.com");
        user.setFirstName("Ana Sofía");
        user.setLastName("Granados Hijo");

        User anotherUser = new User();
        anotherUser.setEmail(email);

        UserDto payload = new UserDto();
        payload.setId(userId);
        payload.setFirstName("Ana Sofía");
        payload.setLastName("Granados Hijo");
        payload.setEmail(email);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail(email)).thenReturn(Optional.of(anotherUser));

        Assertions.assertThatThrownBy(() -> userService.updateUser(payload)).isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateUser_successTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("xugtr1rcsu3jf1hvgwjc@msn.com");
        user.setFirstName("Ana Sofía");
        user.setLastName("Granados Hijo");

        UserDto payload = new UserDto();
        payload.setId(userId);
        payload.setFirstName("Ana Sofía");
        payload.setLastName("Granados Hijo");
        payload.setEmail("xugtr1rcsu3jf1hvgwjc@msn.com");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        UserDto result = userService.updateUser(payload);
        Assertions.assertThat(result).isNotNull().hasFieldOrPropertyWithValue("id", userId);

    }

    @Test
    void getUser_existsTest(){
        UUID userId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);
        user.setEmail("0aq7xkhk1c@yahoo.com");
        user.setFirstName("Daniela");
        user.setLastName("Solorzano");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUser(userId);
        Assertions.assertThat(result).isPresent();

    }

    @Test
    void getUser_doesNotExistTest(){
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.getUser(userId);
        Assertions.assertThat(result).isEmpty();
    }

}
