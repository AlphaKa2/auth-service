package com.alphaka.authservice.security.oauth2.handler;

import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.security.oauth2.user.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.createAccessToken(customOAuth2User.getEmail(), customOAuth2User.getRole());
        String refreshToken = jwtService.createRefreshToken();

        try {
            jwtService.setAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
