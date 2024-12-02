package com.alphaka.authservice.redis.service;

import com.alphaka.authservice.redis.entity.AccessTokenBlacklist;
import com.alphaka.authservice.redis.repository.AccessTokenBlackListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccessTokenBlackListService {

    private final AccessTokenBlackListRepository accessTokenBlackListRepository;

    public void addAccessTokenToBlacklist(String accessToken) {
        log.info("accessToken을 블랙리스트에 추가 {}", accessToken);
        accessTokenBlackListRepository.save(new AccessTokenBlacklist(accessToken));
    }

    public boolean isTokenBlacklisted(String accessToken) {
        return accessTokenBlackListRepository.findById(accessToken).isPresent();
    }
}
