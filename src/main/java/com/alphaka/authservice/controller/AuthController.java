package com.alphaka.authservice.controller;

import com.alphaka.authservice.dto.request.AccessTokenRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.redis.service.AccessTokenBlackListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi{

    private final AccessTokenBlackListService accessTokenBlackListService;

    //게이트웨이에가 요청하는 accessToken 블랙리스트 검증
    @Override
    @PostMapping("/blacklist")
    public ApiResponse<Boolean> blacklist(@RequestBody @Valid AccessTokenRequest request) {
        log.info("게이트웨이로부터 accessToken 블랙리스트 검증 요청 {}", request.getAccessToken());
        boolean tokenBlacklisted = accessTokenBlackListService.isTokenBlacklisted(request.getAccessToken());

        log.info("블랙리스트 검증 값 {}", tokenBlacklisted);
        return ApiResponse.createSuccessResponseWithData(HttpStatus.OK.value(), tokenBlacklisted);
    }
}
