package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.dto.Role;
import com.alphaka.authservice.redis.entity.RefreshToken;
import com.alphaka.authservice.redis.repository.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 14;

    public void saveRefreshToken(String id, String refreshToken) {
        RefreshToken token = new RefreshToken(id, refreshToken, REFRESH_TOKEN_TTL);
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken);
    }

    public void deleteRefreshToken(String id) {
        refreshTokenRepository.deleteById(id);
    }

}
