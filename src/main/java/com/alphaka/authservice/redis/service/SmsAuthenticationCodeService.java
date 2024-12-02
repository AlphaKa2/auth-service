package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.redis.entity.SmsAuthenticationCode;
import com.alphaka.authservice.redis.repository.SmsAuthenticationCodeRepostiory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsAuthenticationCodeService {

    private final long AUTHENTICATION_CODE_TTL = 300L;

    private final SmsAuthenticationCodeRepostiory smsAuthenticationCodeRepostiory;

    public void saveAuthenticationCode(String phoneNumber, String authenticationCode) {
        log.info("전화번호 {}에 대한 인증코드 {} 레디스에 저장", phoneNumber, authenticationCode);
        smsAuthenticationCodeRepostiory.save(
                new SmsAuthenticationCode(phoneNumber, authenticationCode, AUTHENTICATION_CODE_TTL));
    }

    public Optional<SmsAuthenticationCode> getAuthenticationCodeByNumber(String phoneNumber) {
        log.info("전화번호 {}에 대한 인증코드 레디스에 조회", phoneNumber);
        return smsAuthenticationCodeRepostiory.findById(phoneNumber);
    }

}
