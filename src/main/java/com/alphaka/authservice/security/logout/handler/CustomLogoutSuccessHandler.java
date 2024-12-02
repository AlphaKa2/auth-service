package com.alphaka.authservice.security.logout.handler;

import com.alphaka.authservice.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        log.info("로그아웃 성공");

        ApiResponse apiResponse = ApiResponse.createSuccessResponse(HttpStatus.OK.value());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(apiResponse.getStatus());
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
