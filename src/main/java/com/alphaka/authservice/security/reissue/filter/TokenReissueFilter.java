package com.alphaka.authservice.security.reissue.filter;

import com.alphaka.authservice.dto.Role;
import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.entity.RefreshToken;
import com.alphaka.authservice.redis.repository.RefreshTokenRepository;
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
                    throw new Exception();
                }

                String email = maybeRefreshToken.get().getEmail();
                Role role = maybeRefreshToken.get().getRole();
                String newAccessToken = jwtService.createAccessToken(email, role);
                String newRefreshToken = jwtService.createRefreshToken();

                jwtService.setAccessTokenAndRefreshToken(response, newAccessToken, newRefreshToken);
                refreshTokenService.saveRefreshToken(email, newRefreshToken, role);

            } else {
                // 리프레시토큰이 없거나, 유효하지 않음
                throw new Exception();
            }
        } catch (Exception e) {
            response.sendRedirect("http://127.0.0.1:3000");
        }
    }
}
