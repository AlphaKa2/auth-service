package com.alphaka.authservice.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("smsAuthenticationCode")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SmsAuthenticationCode {

    @Id
    String phoneNumber;
    String authenticationCode;

    @TimeToLive
    private long ttl;
}
