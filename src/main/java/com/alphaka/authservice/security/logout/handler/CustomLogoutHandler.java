package com.alphaka.authservice.security.logout.handler;

import static com.alphaka.authservice.util.UserInfoHeader.AUTHENTICATED_USER_ID_HEADER;

import com.alphaka.authservice.jwt.JwtService;
import com.alphaka.authservice.redis.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        String id = request.getHeader(AUTHENTICATED_USER_ID_HEADER.getName());

        //사용자의 리프레시 토큰 쿠키 삭제
        jwtService.expireRefreshTokenCookie(response);

        //레디스에 저장된 리프레시 토큰 삭제
        refreshTokenService.deleteRefreshToken(id);

    }


}
