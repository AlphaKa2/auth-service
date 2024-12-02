package com.alphaka.authservice.kafka.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountDisableProducerService {

    private static final String ACCOUNT_DISABLE_TOPIC = "account-disable";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String email) {
        log.info("반복된 로그인 실패로 인한 계정 비활성화 이벤트 메시지 전송");
        kafkaTemplate.send(ACCOUNT_DISABLE_TOPIC, email);
    }
}
