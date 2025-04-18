package dev.mednikov.accunta.auth.models;

import dev.mednikov.accunta.users.models.User;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auth_authentication_token")
public class AuthenticationToken {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = "token_string", nullable = false, unique = true)
    private String token;

    @Column(name = "expiration_dt", nullable = false)
    private LocalDateTime expiresAt;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof AuthenticationToken that)) return false;

        return user.equals(that.user) && token.equals(that.token);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + token.hashCode();
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
