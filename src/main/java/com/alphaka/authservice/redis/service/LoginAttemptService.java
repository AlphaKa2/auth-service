package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.kafka.service.AccountDisableProducerService;
import com.alphaka.authservice.redis.entity.LoginAttempt;
import com.alphaka.authservice.redis.repository.LoginAttemptRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5; // 최대 로그인 실패 횟수

    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountDisableProducerService accountDisableProducerService;

    public void loginFail(String email) {
        Optional<LoginAttempt> maybeLoginAttempt = loginAttemptRepository.findById(email);

        if (maybeLoginAttempt.isEmpty()) {
            log.info("계정({})에 대한 현재 로그인 실패 횟수: 1", email);
            loginAttemptRepository.save(new LoginAttempt(email, 1));
            return;
        }

        LoginAttempt loginAttempt = maybeLoginAttempt.get();
        int count = loginAttempt.incrementCount();

        log.info("계정({})의 현재 로그인 실패 횟수: {}", email, count);


        if (count == MAX_ATTEMPTS) {
            log.info("계정({})에 대한 로그인 실패 횟수가 임계값에 도달했습니다.", email);
            accountDisableProducerService.sendMessage(email);
        }

        loginAttemptRepository.save(loginAttempt);
    }

    public void loginSuccess(String email) {
        log.info("계정({}) 로그인 성공, 로그인 실패 횟수 삭제", email);
        loginAttemptRepository.deleteById(email);
    }

}
