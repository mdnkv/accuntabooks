package dev.mednikov.accunta.users.it;

import dev.mednikov.accunta.BaseIT;
import dev.mednikov.accunta.users.dto.CreateUserRequestDto;
import dev.mednikov.accunta.users.dto.UserDto;
import dev.mednikov.accunta.users.models.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.Optional;
import java.util.UUID;

class UserIT extends BaseIT {

    @Test
    void createUser_alreadyExistsTest(){
        String email = "ahdwbo45w2f9y@ymail.com";
        createUser(email);

        CreateUserRequestDto payload = new CreateUserRequestDto();
        payload.setEmail(email);
        payload.setPassword("password");
        payload.setFirstName("Isidora Renata");
        payload.setLastName("Vallejo Mercado");

        HttpEntity<CreateUserRequestDto> request = new HttpEntity<>(payload);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/create", HttpMethod.POST, request, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createUser_successTest(){
        CreateUserRequestDto payload = new CreateUserRequestDto();
        payload.setEmail("zmlu2sjcpptx31m03nn@ymail.com");
        payload.setPassword("password");
        payload.setFirstName("Renata Valery");
        payload.setLastName("Jiménez Centeno");

        HttpEntity<CreateUserRequestDto> request = new HttpEntity<>(payload);
        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/create", HttpMethod.POST, request, UserDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();

        UserDto body = response.getBody();
        Assertions.assertThat(body).hasFieldOrProperty("id");
        UUID userId = body.getId();

        Optional<User> result = userRepository.findById(userId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void updateUser_EmailAlreadyExistsTest(){
        String email = "x1zau33kjx24g@comcast.net";
        createUser(email);

        User user = createUser("4m1t6h5gytpidc@ymail.com");
        String token = createAuthenticationToken(user);

        UserDto payload = new UserDto();
        payload.setEmail(email);
        payload.setId(user.getId());
        payload.setFirstName("Josefa Rael");
        payload.setLastName("Sofía Quintero");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<UserDto> request = new HttpEntity<>(payload, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/update", HttpMethod.PUT, request, UserDto.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void updateUser_successTest(){
        User user = createUser("940rtoa95iy4@ymail.com");
        String token = createAuthenticationToken(user);

        UserDto payload = new UserDto();
        payload.setEmail("940rtoa95iy4@ymail.com");
        payload.setId(user.getId());
        payload.setFirstName("Josefa Rael");
        payload.setLastName("Sofía Quintero");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<UserDto> request = new HttpEntity<>(payload, headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/update", HttpMethod.PUT, request, UserDto.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        UserDto body = response.getBody();
        Assertions.assertThat(body)
                .hasFieldOrProperty("id")
                .hasFieldOrPropertyWithValue("email", "940rtoa95iy4@ymail.com")
                .hasFieldOrPropertyWithValue("firstName", "Josefa Rael")
                .hasFieldOrPropertyWithValue("lastName", "Sofía Quintero");
    }

    @Test
    void deleteUserTest(){
        User user = createUser("y51wl4mn9rongros@msn.com");
        String token = createAuthenticationToken(user);
        UUID userId = user.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/users/delete/{userId}", HttpMethod.DELETE, request, Void.class, userId
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<User> result = userRepository.findById(userId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getUser_existsTest(){
        User user = createUser("amani.garcia@comcast.net");
        String token = createAuthenticationToken(user);

        User anotherUser = createUser("3zchb05z0wfwwzp91a@ymail.com");
        UUID userId = anotherUser.getId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/user/{userId}", HttpMethod.GET, request, UserDto.class, userId
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getUser_doesNotExistTest(){
        User user = createUser("refgp9psd9twa72v9fiy@comcast.net");
        String token = createAuthenticationToken(user);

        UUID userId = UUID.randomUUID();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> response = restTemplate.exchange(
                "/api/users/user/{userId}", HttpMethod.GET, request, UserDto.class, userId
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
