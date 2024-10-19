package com.alphaka.authservice.redis.repository;

import com.alphaka.authservice.redis.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findRefreshTokenByRefreshToken(String refreshToken);
}
