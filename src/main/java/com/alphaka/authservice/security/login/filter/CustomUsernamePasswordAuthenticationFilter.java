package com.alphaka.authservice.security.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERNAME = "email";
    private static final String PASSWORD = "password";

    private final ObjectMapper objectMapper;
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException(
                    "Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = usernamePasswordMap.get(USERNAME);
        String password = usernamePasswordMap.get(PASSWORD);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email,
                password);

        //로그아웃 실패 시, 이메일 정보를 사용하기 위해 임시 저장
        request.setAttribute("X-Login-Attempt-Email", email);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
