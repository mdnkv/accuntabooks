package dev.mednikov.accunta;

import dev.mednikov.accunta.auth.models.AuthenticationToken;
import dev.mednikov.accunta.auth.repositories.AuthenticationTokenRepository;
import dev.mednikov.accunta.auth.utils.AuthTokenUtils;
import dev.mednikov.accunta.users.models.User;
import dev.mednikov.accunta.users.repositories.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {

    @Autowired protected UserRepository userRepository;
    @Autowired protected AuthenticationTokenRepository authenticationTokenRepository;
    @Autowired protected TestRestTemplate restTemplate;

    private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:17")
            .withDatabaseName("accunta_db")
            .withUsername("user")
            .withPassword("secret");

    @DynamicPropertySource
    static void setContainerProperty(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @BeforeAll
    static void startContainers() {
        container.start();
    }

    protected User createUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("secret");
        return userRepository.save(user);
    }

    protected String createAuthenticationToken (User user){
        String tokenString = AuthTokenUtils.generateNewToken();

        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(tokenString);
        authenticationToken.setUser(user);
        authenticationToken.setExpiresAt(LocalDateTime.now().plusHours(1));
        authenticationTokenRepository.save(authenticationToken);

        return tokenString;
    }



}
