package dev.mednikov.accunta.auth.it;

import dev.mednikov.accunta.BaseIT;
import dev.mednikov.accunta.auth.dto.LoginRequestDto;
import dev.mednikov.accunta.auth.dto.LoginResponseDto;
import dev.mednikov.accunta.auth.models.AuthenticationToken;
import dev.mednikov.accunta.users.models.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthIT extends BaseIT {

    @Autowired private PasswordEncoder passwordEncoder;

    @Test
    void login_invalidEmailTest(){
        LoginRequestDto payload = new LoginRequestDto();
        payload.setEmail("o22q3wc31le@outlook.com");
        payload.setPassword("password");

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(payload);
        ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                "/api/auth/login", HttpMethod.POST, request, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_invalidPasswordTest(){
        String password = passwordEncoder.encode("password1234");
        User user = new User();
        user.setEmail("7dewzfvsbd13th@yahoo.com");
        user.setPassword(password);
        user.setFirstName("Josefa Maite");
        user.setLastName("Toledo Olivas Hijo");
        userRepository.save(user);

        LoginRequestDto payload = new LoginRequestDto();
        payload.setEmail("7dewzfvsbd13th@yahoo.com");
        payload.setPassword("password");

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(payload);
        ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                "/api/auth/login", HttpMethod.POST, request, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void login_successTest(){
        String password = passwordEncoder.encode("password1234");
        User user = new User();
        user.setEmail("z7l1sf9hlxt@googlemail.com");
        user.setPassword(password);
        user.setFirstName("Ximena Laura");
        user.setLastName("Quiñónez Puente");
        userRepository.save(user);

        LoginRequestDto payload = new LoginRequestDto();
        payload.setEmail("z7l1sf9hlxt@googlemail.com");
        payload.setPassword("password1234");

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(payload);
        ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                "/api/auth/login", HttpMethod.POST, request, LoginResponseDto.class
        );

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        LoginResponseDto body = response.getBody();
        Assertions.assertThat(body).hasFieldOrProperty("token").hasFieldOrProperty("userId");

        Optional<AuthenticationToken> authenticationToken = authenticationTokenRepository.findByToken(body.getToken());
        Assertions.assertThat(authenticationToken).isPresent();
    }

    @Test
    void logoutTest(){
        User user = createUser("tn21nmcwvel90oipc5l@aol.com");
        String token = createAuthenticationToken(user);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token ".concat(token));
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(
                "/api/auth/logout", HttpMethod.POST, requestEntity, Void.class
        );

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Optional<AuthenticationToken> authenticationToken = authenticationTokenRepository.findByToken(token);
        Assertions.assertThat(authenticationToken).isEmpty();
    }

}
