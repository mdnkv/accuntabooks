package dev.mednikov.accunta.auth.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import dev.mednikov.accunta.auth.models.AuthenticatedPrincipal;
import dev.mednikov.accunta.auth.services.AuthService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilterImpl implements AuthenticationFilter {

    private final AuthService authService;

    public AuthenticationFilterImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Token ")) {
            String token = authHeader.substring(6);
            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                Optional<AuthenticatedPrincipal> result = this.authService.authorize(token);

                if (result.isPresent()){
                    AuthenticatedPrincipal principal = result.get();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            principal, null, principal.getAuthorities()
                    );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            }
        }
        filterChain.doFilter(request, servletResponse);
    }
}
