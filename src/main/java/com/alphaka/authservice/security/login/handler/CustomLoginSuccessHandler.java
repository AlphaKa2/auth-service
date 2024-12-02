package com.alphaka.authservice.security.login.handler;

import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.service.LoginAttemptService;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import com.alphaka.authservice.security.login.user.CustomUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomUser userDetails = (CustomUser) authentication.getPrincipal();

        Long id = Long.parseLong(userDetails.getUsername());
        log.info("유저 {} 자체 로그인 성공", id);

        String accessToken = jwtService.createAccessToken(id, userDetails.getNickname(),
                userDetails.getProfileImage(), userDetails.getRole());
        String refreshToken = jwtService.createRefreshToken();
        String email = (String) request.getAttribute("X-Login-Attempt-Email");

        try {
            jwtService.setAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        refreshTokenService.saveRefreshToken(String.valueOf(id), refreshToken);
        loginAttemptService.loginSuccess(email);
    }
}
