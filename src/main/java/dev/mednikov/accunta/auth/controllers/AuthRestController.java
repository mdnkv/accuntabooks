package dev.mednikov.accunta.auth.controllers;

import dev.mednikov.accunta.auth.dto.LoginRequestDto;
import dev.mednikov.accunta.auth.dto.LoginResponseDto;
import dev.mednikov.accunta.auth.models.AuthenticatedPrincipal;
import dev.mednikov.accunta.auth.services.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public @ResponseBody LoginResponseDto login (@RequestBody LoginRequestDto payload) {
        return this.authService.login(payload);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(Authentication authentication) {
        AuthenticatedPrincipal principal = (AuthenticatedPrincipal) authentication.getPrincipal();
        this.authService.logout(principal);
    }

}
