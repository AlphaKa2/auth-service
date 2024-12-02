package com.alphaka.authservice.reissue.filter;

import com.alphaka.authservice.dto.response.UserSignInResponse;
import com.alphaka.authservice.exception.custom.InvalidRefreshTokenException;
import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.openfeign.UserServiceClient;
import com.alphaka.authservice.redis.entity.RefreshToken;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenReissueFilter extends OncePerRequestFilter {

    private static final String DEFAULT_REISSUE_REQUEST_URL = "/reissue";

    private final JwtService jwtService;
    private final UserServiceClient userServiceClient;
    private final RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!request.getRequestURI().equals(DEFAULT_REISSUE_REQUEST_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            log.info("토큰 재발급 필터 시작");

            Optional<String> maybeRefreshTokenFromRequest = jwtService.extractRefreshToken(request);

            if (maybeRefreshTokenFromRequest.isPresent() && jwtService.isValidToken(
                    maybeRefreshTokenFromRequest.get())) {

                Optional<RefreshToken> maybeRefreshToken = refreshTokenService.findByRefreshToken(
                        maybeRefreshTokenFromRequest.get());

                // 레디스에 해당 리프레시토큰이 없는 경우
                if (maybeRefreshToken.isEmpty()) {
                    log.error("레디스에 존재하지 않는 refreshToken입니다.");
                    throw new InvalidRefreshTokenException();
                }

                Long userId = Long.parseLong(maybeRefreshToken.get().getId());

                log.info("유저{}의 새로운 accessToken, refreshToken 생성", userId);
                UserSignInResponse user = userServiceClient.user(userId).getData();
                String newAccessToken = jwtService.createAccessToken(userId, user.getNickname(),
                        user.getProfileImage(), user.getRole());
                String newRefreshToken = jwtService.createRefreshToken();

                jwtService.setAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
                refreshTokenService.saveRefreshToken(String.valueOf(userId), newRefreshToken);

                log.info("토큰 재발급 성공");
            } else {
                log.error("토큰 재발급이 불가능합니다.");
                throw new InvalidRefreshTokenException();
            }
        } catch (Exception e) {
            log.error("로그인 페이지로 리다이렉션");
            response.sendRedirect("http://127.0.0.1:3000");
        }
    }
}
