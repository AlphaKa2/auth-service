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
import org.springframework.http.ResponseCookie;
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

    @Value("${jwt.smsConfirmation.expiration}")
    private Long smsConfirmationExpirationPeriod;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String SMS_Confirmation_TOKEN_SUBJECT = "SmsConfirmation";
    private static final String ID_CLAIM = "id";
    private static final String ROLE_CLAIM = "role";
    private static final String NICKNAME_CLAIM = "nickname";
    private static final String PROFILE_CLAIM = "profile";
    private static final String PHONE_NUMBER_CLAIM = "phoneNumber";


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

    public String createSmsConfirmationToken(String phoneNumber) {
        Date now = new Date();
        return Jwts
                .builder()
                .subject(SMS_Confirmation_TOKEN_SUBJECT)
                .expiration(new Date(now.getTime() + smsConfirmationExpirationPeriod))
                .claim(PHONE_NUMBER_CLAIM, phoneNumber)
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
        log.info("사용자의 refreshToken 쿠키 만료");
        response.addCookie(refreshTokenCookie);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        log.info("사용자의 refreshToken 추출 시도");
        return Optional.ofNullable(findCookieByName(request, REFRESH_TOKEN_COOKIE));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(ACCESS_TOKEN_HEADER))
                .filter(token -> token.startsWith(BEARER))
                .map(token -> token.replace(BEARER, ""));
    }

    public void setAccessTokenAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken)
            throws Exception {
        log.info("응답에 accessToken, refreshToken 추가");

        int refreshTokenCookieMaxAge = 14 * 24 * 60 * 60;

        ResponseCookie refreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .path("/")
                .maxAge(refreshTokenCookieMaxAge)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        // 일반 쿠키 추가
        ResponseCookie regularCookie = ResponseCookie.from("TEST_COOKIE", "test_value")
                .path("/")
                .maxAge(refreshTokenCookieMaxAge)
                .sameSite("None") // Cross-Origin을 위한 설정
                .build();

        response.addHeader("Set-Cookie", regularCookie.toString());


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
            log.info("토큰 {}에 대한 검증 시작", token);

            Jws<Claims> claims = Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            if (!claims.getPayload().getExpiration().after(new Date())) {
                log.info("토큰의 유효기간이 지났습니다. {}", token);
                return false;
            }
            return true;
        } catch (JwtException e) {
            log.error("토큰 해독 중 문제가 발생했습니다. {}", token);
            log.error("{}", e.getMessage());
            return false;
        }
    }

    private static String findCookieByName(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            String cookieName = cookies[i].getName();
            String value = cookies[i].getValue();
            log.info("쿠키 명:{} 값:{}", cookieName, value);
            if (cookieName.equals(name)) {
                return value;
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
