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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
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

            Optional<String> maybeRefreshTokenFromRequest = jwtService.extractRefreshToken(request);

            if (maybeRefreshTokenFromRequest.isPresent() && jwtService.isValidToken(
                    maybeRefreshTokenFromRequest.get())) {
                Optional<RefreshToken> maybeRefreshToken = refreshTokenService.findByRefreshToken(
                        maybeRefreshTokenFromRequest.get());

                // 레디스에 리프레시토큰이 없거나, 요청에 들어온 토큰과 다름
                if (maybeRefreshToken.isEmpty() || !maybeRefreshToken.get().getRefreshToken()
                        .equals(maybeRefreshTokenFromRequest.get())) {
                    throw new InvalidRefreshTokenException();
                }

                Long userId = Long.parseLong(maybeRefreshToken.get().getId());

                UserSignInResponse user = userServiceClient.user(userId).getData();
                String newAccessToken = jwtService.createAccessToken(userId, user.getNickname(),
                        user.getProfileImage(), user.getRole());
                String newRefreshToken = jwtService.createRefreshToken();

                jwtService.setAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
                refreshTokenService.saveRefreshToken(String.valueOf(userId), newRefreshToken);

            } else {
                throw new InvalidRefreshTokenException();
            }
        } catch (Exception e) {
            response.sendRedirect("http://127.0.0.1:3000");
        }
    }
}
