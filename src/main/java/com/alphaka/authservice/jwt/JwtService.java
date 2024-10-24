package com.alphaka.authservice.jwt;

import com.alphaka.authservice.dto.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.common.contenttype.ContentType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokeExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "role";
    private static final String NICKNAME_CLAIM = "nickname";
    private static final String PROFILE_CLAIM = "profile";


    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_COOKIE = "Refresh";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private SecretKey key;

    @PostConstruct
    void initializeKey() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(Long id, String nickname, String profile, Role role) {
        Date now = new Date();

        return Jwts
                .builder()
                .subject(ACCESS_TOKEN_SUBJECT)
                .claim(ID_CLAIM, id)
                .claim(NICKNAME_CLAIM, nickname)
                .claim(PROFILE_CLAIM, profile)
                .claim(ROLE_CLAIM, role)
                .expiration(new Date(now.getTime() + accessTokeExpirationPeriod))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts
                .builder()
                .subject(REFRESH_TOKEN_SUBJECT)
                .expiration(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .signWith(key)
                .compact();
    }

    public void expireRefreshTokenCookie(HttpServletResponse response) {

        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        refreshTokenCookie.setHttpOnly(true);
        //refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");

        // 응답에 쿠키를 추가하여 삭제
        response.addCookie(refreshTokenCookie);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(findCookieByName(request, REFRESH_TOKEN_COOKIE));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_HEADER))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public void setAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken)
            throws Exception {
        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        int refreshTokenCookieMaxAge = 14 * 24 * 60 * 60;

        refreshTokenCookie.setHttpOnly(true);
        //refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setMaxAge(refreshTokenCookieMaxAge);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);

        response.setContentType(ContentType.APPLICATION_JSON.getType());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Claims claims = decodeAccessToken(accessToken);

        AuthenticationResponse authenticationResponse =
                AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .userId(claims.get(ID_CLAIM, Long.class))
                        .nickname(claims.get(NICKNAME_CLAIM, String.class))
                        .profileImageUrl(claims.get(PROFILE_CLAIM, String.class))
                        .build();

        String jsonResponse = objectMapper.writeValueAsString(authenticationResponse);
        response.getWriter().write(jsonResponse);
    }

    private Claims decodeAccessToken(String accessToken) {

        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return claims.getPayload().getExpiration().after(new Date());
        } catch (JwtException e) {
            log.error("Invalid Token: {}", e.getMessage());
            return false;
        }
    }

    private static String findCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals(name)) {
                return cookies[i].getValue();
            }
        }
        return null;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter
    @Setter
    private static class AuthenticationResponse {

        Long userId;
        String nickname;
        String profileImageUrl;
        String accessToken;
    }
}
