package com.alphaka.authservice.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "LoginAttempt", timeToLive = 300) //5분 동안 기록
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginAttempt {

    @Id
    private String email;
    private int count;

    public int incrementCount() {
        return ++count;
    }

}
