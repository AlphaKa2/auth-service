package com.alphaka.authservice.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("RefreshToken")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshToken {

    //레디스에선 키로 사용할 값이 문자열이어야 한다.
    @Id
    private String id;
    @Indexed
    private String refreshToken;

    @TimeToLive
    private long ttl;
}
