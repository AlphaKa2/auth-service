package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.redis.entity.SmsAuthenticationCode;
import com.alphaka.authservice.redis.repository.SmsAuthenticationCodeRepostiory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SmsAuthenticationCodeService {

    private final long AUTHENTICATION_CODE_TTL = 300L;

    private final SmsAuthenticationCodeRepostiory smsAuthenticationCodeRepostiory;

    public void saveAuthenticationCode(String phoneNumber, String authenticationCode) {
        smsAuthenticationCodeRepostiory.save(
                new SmsAuthenticationCode(phoneNumber, authenticationCode, AUTHENTICATION_CODE_TTL));
    }

    public Optional<SmsAuthenticationCode> getAuthenticationCodeByNumber(String phoneNumber) {
        return smsAuthenticationCodeRepostiory.findById(phoneNumber);
    }

}
