package com.alphaka.authservice.security.login.handler;

import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import com.alphaka.authservice.security.login.user.CustomUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Long id = Long.parseLong(userDetails.getUsername());

        String accessToken = jwtService.createAccessToken(id, userDetails.getNickname(),
                userDetails.getProfileImage(), userDetails.getRole());
        String refreshToken = jwtService.createRefreshToken();

        try {
            jwtService.setAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        refreshTokenService.saveRefreshToken(String.valueOf(id), refreshToken);
    }
}
