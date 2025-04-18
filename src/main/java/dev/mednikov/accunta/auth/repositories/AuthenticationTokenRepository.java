package dev.mednikov.accunta.auth.repositories;

import dev.mednikov.accunta.auth.models.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, UUID> {

    Optional<AuthenticationToken> findByToken(String token);

    @Query("DELETE FROM AuthenticationToken at WHERE at.user.id = :userId")
    @Modifying
    void deleteTokensForUser(UUID userId);

}
