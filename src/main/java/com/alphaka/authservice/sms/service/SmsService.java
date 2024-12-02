package com.alphaka.authservice.sms.service;

import com.alphaka.authservice.exception.custom.SmsVerificationFailureException;
import com.alphaka.authservice.redis.entity.SmsAuthenticationCode;
import com.alphaka.authservice.redis.service.SmsAuthenticationCodeService;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String numner;

    @Value("${coolsms.api.url}")
    private String apiUrl;

    private final SmsAuthenticationCodeService smsAuthenticationCodeService;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }


    public void sendAuthenticationMessage(String destination) {
        Message message = new Message();
        String authenticationCode = generateAuthenticationCode();

        // 수신번호, 발신번호는 01012345678 형태여야만 한다
        message.setFrom(numner);
        message.setTo(destination);
        message.setText(authenticationCode);

        log.info("전화번호 {}에 대한 인증코드 {} 생성 및 전송", destination, authenticationCode);

        messageService.sendOne(new SingleMessageSendingRequest(message));
        smsAuthenticationCodeService.saveAuthenticationCode(destination, authenticationCode);
    }

    public void verifyAuthenticationCode(String destination, String authenticationCode) {

        SmsAuthenticationCode validAuthenticationCode = smsAuthenticationCodeService
                .getAuthenticationCodeByNumber(destination)
                .orElseThrow(() -> {
                    log.error("해당 전화번호({})에 대한 유효한 인증 코드가 존재하지 않습니다.", destination);
                    return new SmsVerificationFailureException();
                });

        log.info("전화번호({})에 대해 입력된 인증 코드: {}", destination, authenticationCode);
        log.info("유효한 인증 코드: {}", validAuthenticationCode.getAuthenticationCode());

        if (!validAuthenticationCode.getAuthenticationCode().equals(authenticationCode)) {
            log.error("전화번호({})에 대한 일치하지 않는 인증 코드({}) 입니다.", destination, authenticationCode);
            throw new SmsVerificationFailureException();
        }

    }

    private String generateAuthenticationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
