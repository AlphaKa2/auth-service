package com.alphaka.authservice.security.login.handler;

import com.alphaka.authservice.exception.custom.AuthenticationFailureException;
import com.alphaka.authservice.redis.service.LoginAttemptService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("자체 로그인 실패: {}", exception.getMessage());

        // 비밀번호 틀린 경우
        if (exception instanceof BadCredentialsException) {
            String email = (String) request.getAttribute("X-Login-Attempt-Email");
            log.info("틀린 비밀번호 입니다.");
            loginAttemptService.loginFail(email);
        }

        throw new AuthenticationFailureException();
    }
}
