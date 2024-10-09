package com.alphaka.authservice.redis.service;

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

    public void saveRefreshToken(String email, String refreshToken) {
        RefreshToken token = new RefreshToken(email, refreshToken, REFRESH_TOKEN_TTL);
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByEmail(String email) {
        return refreshTokenRepository.findById(email);
    }

    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteById(email);
    }

}
