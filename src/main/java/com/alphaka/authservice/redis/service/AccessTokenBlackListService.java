package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.redis.entity.AccessTokenBlacklist;
import com.alphaka.authservice.redis.repository.AccessTokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenBlackListService {

    private final AccessTokenBlackListRepository accessTokenBlackListRepository;

    public void addAccessTokenToBlacklist(String accessToken) {
        accessTokenBlackListRepository.save(new AccessTokenBlacklist(accessToken));
    }

    public boolean isTokenBlacklisted(String accessToken) {
        return accessTokenBlackListRepository.findById(accessToken).isPresent();
    }
}
