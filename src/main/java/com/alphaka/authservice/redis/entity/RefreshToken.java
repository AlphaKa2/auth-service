package com.alphaka.authservice.redis.entity;

import com.alphaka.authservice.dto.Role;
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

    @Id
    private String email;
    @Indexed
    private String refreshToken;
    private Role role;

    @TimeToLive
    private long ttl;
}
