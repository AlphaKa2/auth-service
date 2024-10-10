package com.alphaka.authservice.sms.service;

import com.alphaka.authservice.redis.entity.SmsAuthenticationCode;
import com.alphaka.authservice.redis.service.SmsAuthenticationCodeService;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
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

        messageService.sendOne(new SingleMessageSendingRequest(message));
        smsAuthenticationCodeService.saveAuthenticationCode(destination, authenticationCode);
    }

    public boolean verifyAuthenticationCode(String destination, String authenticationCode) {
        Optional<SmsAuthenticationCode> maybeAuthenticationCode =
                smsAuthenticationCodeService.getAuthenticationCodeByNumber(destination);

        if (maybeAuthenticationCode.isEmpty() ||
                !maybeAuthenticationCode.get().getAuthenticationCode().equals(authenticationCode)) {
            return false;
        }

        return true;
    }

    private String generateAuthenticationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
