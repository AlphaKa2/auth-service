package com.alphaka.authservice.security.login.handler;

import com.alphaka.authservice.dto.Role;
import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        String accessToken = jwtService.createAccessToken(email, Role.USER);
        String refreshToken = jwtService.createRefreshToken();

        try {
            jwtService.setAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        refreshTokenService.saveRefreshToken(email, refreshToken);
    }
}
