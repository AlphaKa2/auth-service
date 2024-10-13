package com.alphaka.authservice.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDisableProducerService {

    private static final String ACCOUNT_DISABLE_TOPIC = "account-disable";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String email) {
        kafkaTemplate.send(ACCOUNT_DISABLE_TOPIC, email);
    }
}
