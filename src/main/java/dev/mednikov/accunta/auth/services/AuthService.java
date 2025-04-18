package dev.mednikov.accunta.auth.services;

import dev.mednikov.accunta.auth.dto.LoginRequestDto;
import dev.mednikov.accunta.auth.dto.LoginResponseDto;
import dev.mednikov.accunta.auth.models.AuthenticatedPrincipal;

import java.util.Optional;

public interface AuthService {

    LoginResponseDto login (LoginRequestDto payload);

    Optional<AuthenticatedPrincipal> authorize (String credentials);

    void logout (AuthenticatedPrincipal principal);

}
