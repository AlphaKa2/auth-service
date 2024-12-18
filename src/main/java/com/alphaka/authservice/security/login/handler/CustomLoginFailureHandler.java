package com.alphaka.authservice.security.login.handler;

import com.alphaka.authservice.dto.response.ErrorResponse;
import com.alphaka.authservice.exception.ErrorCode;
import com.alphaka.authservice.redis.service.LoginAttemptService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginAttemptService loginAttemptService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("자체 로그인 실패: {}", exception.getMessage());

        ErrorCode errorCode;
        ErrorResponse errorResponse;

        // 인증 시도 중 문제 발생, 스프링 시큐리티가 오픈페인 시큐리티 예외로 래핑하여 던짐
        if (exception instanceof InternalAuthenticationServiceException) {
            log.error("인증 시도 중 문제가 발생했습니다.");
            log.error("다른 서비스의 상태를 확인해주세요.");

            errorCode = ErrorCode.AUTHENTICATION_SERVICE_FAILURE;
            errorResponse = new ErrorResponse(
                    errorCode.status(),
                    errorCode.code(),
                    errorCode.message()
            );

            setHttpResponse(response, errorResponse);
            return;
        }

        if (exception instanceof UsernameNotFoundException) {
            log.error("존재하지 않는 사용자입니다.");

        } else if (exception instanceof BadCredentialsException) {
            log.info("틀린 비밀번호입니다.");
            String email = (String) request.getAttribute("X-Login-Attempt-Email");
            loginAttemptService.loginFail(email);

        }

        // 보안 문제로 이메일과 비밀번호 중 무엇이 틀렸는지는 외부에 노출하지 않는다.
        errorCode = ErrorCode.INVALID_EMAIL_OR_PASSWORD;
        errorResponse = new ErrorResponse(
                errorCode.status(),
                errorCode.code(),
                errorCode.message()
        );

        setHttpResponse(response, errorResponse);

    }

    private void setHttpResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
