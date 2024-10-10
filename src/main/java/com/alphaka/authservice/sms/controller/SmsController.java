package com.alphaka.authservice.sms.controller;


import com.alphaka.authservice.dto.request.SmsAuthenticationRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.sms.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;

    //인증 코드 sms 전송
    @PostMapping("/sms/authentication")
    public ApiResponse sendAuthenticationCode(@RequestBody @Valid SmsAuthenticationRequest request) {
        smsService.sendAuthenticationMessage(request.getPhoneNumber());
        return ApiResponse.createSuccessResponse(HttpStatus.OK.value());
    }

    @PostMapping("/sms/verification")
    public ApiResponse verifyAuthenticationCode(@RequestBody @Valid SmsAuthenticationRequest request) {
        if (smsService.verifyAuthenticationCode(request.getPhoneNumber(), request.getAuthenticationCode())) {
            return ApiResponse.createSuccessResponse(HttpStatus.ACCEPTED.value());
        }
        return ApiResponse.createErrorResponseWithExceptions(HttpStatus.NOT_ACCEPTABLE.value(),
                new IllegalArgumentException("인증에 실패하였습니다.").getMessage());
    }

}
