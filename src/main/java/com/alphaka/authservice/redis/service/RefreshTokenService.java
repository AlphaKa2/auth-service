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

    public void saveRefreshToken(String email, String refreshToken, Role role) {
        RefreshToken token = new RefreshToken(email, refreshToken, role, REFRESH_TOKEN_TTL);
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByEmail(String email) {
        return refreshTokenRepository.findById(email);
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteById(email);
    }

}
