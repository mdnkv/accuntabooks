package dev.mednikov.accunta.auth.services;

import dev.mednikov.accunta.auth.dto.LoginRequestDto;
import dev.mednikov.accunta.auth.dto.LoginResponseDto;
import dev.mednikov.accunta.auth.exceptions.InvalidCredentialsException;
import dev.mednikov.accunta.auth.models.AuthenticatedPrincipal;
import dev.mednikov.accunta.auth.models.AuthenticationToken;
import dev.mednikov.accunta.auth.repositories.AuthenticationTokenRepository;
import dev.mednikov.accunta.auth.utils.AuthTokenUtils;
import dev.mednikov.accunta.users.models.User;
import dev.mednikov.accunta.users.repositories.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationTokenRepository authenticationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationTokenRepository authenticationTokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResponseDto login(LoginRequestDto payload) {
        User user = this.userRepository.findByEmail(payload.getEmail()).orElseThrow(InvalidCredentialsException::new);
        if (!this.passwordEncoder.matches(payload.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        String token = AuthTokenUtils.generateNewToken();
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(token);
        authenticationToken.setUser(user);
        authenticationToken.setExpiresAt(LocalDateTime.now().plusHours(12));
        this.authenticationTokenRepository.save(authenticationToken);

        LoginResponseDto result = new LoginResponseDto();
        result.setToken(token);
        result.setUserId(user.getId());
        return result;
    }

    @Override
    public Optional<AuthenticatedPrincipal> authorize(String credentials) {
        Optional<AuthenticationToken> result = this.authenticationTokenRepository.findByToken(credentials);
        if (result.isPresent()){
            AuthenticationToken authenticationToken = result.get();
            if (authenticationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                return Optional.empty();
            }
            User user = authenticationToken.getUser();
            AuthenticatedPrincipal principal = new AuthenticatedPrincipal(user);
            return Optional.of(principal);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void logout(AuthenticatedPrincipal principal) {
        UUID userId = principal.getUser().getId();
        this.authenticationTokenRepository.deleteTokensForUser(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> result = this.userRepository.findByEmail(username);
        if (result.isPresent()) {
            User user = result.get();
            return new AuthenticatedPrincipal(user);
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
