package com.alphaka.authservice.security.oauth2.handler;

import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import com.alphaka.authservice.security.oauth2.user.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("소셜 로그인 성공");

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.createAccessToken(customOAuth2User.getId(), customOAuth2User.getNickname(),
                customOAuth2User.getProfileImage(), customOAuth2User.getRole());
        String refreshToken = jwtService.createRefreshToken();

        try {
            jwtService.setAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        refreshTokenService.saveRefreshToken(String.valueOf(customOAuth2User.getId()), refreshToken);
    }
}
