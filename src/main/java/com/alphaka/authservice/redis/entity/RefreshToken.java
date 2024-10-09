package com.alphaka.authservice.redis.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("RefreshToken")
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    private String email;
    private String refreshToken;

    @TimeToLive
    private long ttl;
}
