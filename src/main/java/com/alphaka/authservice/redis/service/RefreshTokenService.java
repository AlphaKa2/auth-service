package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.redis.entity.RefreshToken;
import com.alphaka.authservice.redis.repository.RefreshTokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 14;

    public void saveRefreshToken(String id, String refreshToken) {
        log.info("유저 {}의 refreshToken({}) 레디스에 저장", id, refreshToken);
        RefreshToken token = new RefreshToken(id, refreshToken, REFRESH_TOKEN_TTL);
        refreshTokenRepository.save(token);
    }

    public Optional<RefreshToken> findByRefreshToken(String refreshToken) {
        log.info("refreshToken({}) 레디스에 조회", refreshToken);
        return refreshTokenRepository.findRefreshTokenByRefreshToken(refreshToken);
    }

    public void deleteRefreshToken(String id) {
        log.info("유저 {}의 refreshToken 레디스에 삭제", id);
        refreshTokenRepository.deleteById(id);
    }

}
