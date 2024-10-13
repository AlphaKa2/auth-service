package com.alphaka.authservice.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "AccessTokenBlacklist", timeToLive = 3600)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBlacklist {

    @Id
    private String accessToken;
}
