package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.kafka.service.AccountDisableProducerService;
import com.alphaka.authservice.redis.entity.LoginAttempt;
import com.alphaka.authservice.redis.repository.LoginAttemptRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5; // 최대 로그인 실패 횟수

    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountDisableProducerService accountDisableProducerService;

    public void loginFail(String email) {
        Optional<LoginAttempt> maybeLoginAttempt = loginAttemptRepository.findById(email);

        if (maybeLoginAttempt.isEmpty()) {
            loginAttemptRepository.save(new LoginAttempt(email, 1));
            return;
        }

        LoginAttempt loginAttempt = maybeLoginAttempt.get();
        int count = loginAttempt.incrementCount();

        if (count == MAX_ATTEMPTS) {
            accountDisableProducerService.sendMessage(email);
        }

        loginAttemptRepository.save(loginAttempt);
    }

    public void loginSuccess(String email) {
        loginAttemptRepository.deleteById(email);
    }

}
