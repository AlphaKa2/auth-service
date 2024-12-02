package com.alphaka.authservice.controller;

import com.alphaka.authservice.dto.request.AccessTokenRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.redis.service.AccessTokenBlackListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi{

    private final AccessTokenBlackListService accessTokenBlackListService;

    //게이트웨이에가 요청하는 accessToken 블랙리스트 검증
    @Override
    @PostMapping("/blacklist")
    public ApiResponse<Boolean> blacklist(@RequestBody @Valid AccessTokenRequest request) {
        boolean tokenBlacklisted = accessTokenBlackListService.isTokenBlacklisted(request.getAccessToken());
        return ApiResponse.createSuccessResponseWithData(HttpStatus.OK.value(), tokenBlacklisted);
    }
}
