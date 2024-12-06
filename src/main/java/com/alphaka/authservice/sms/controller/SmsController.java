package com.alphaka.authservice.sms.controller;


import com.alphaka.authservice.dto.request.SmsAuthenticationRequest;
import com.alphaka.authservice.dto.request.SmsVerificationRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.dto.response.SmsVerificationResponse;
import com.alphaka.authservice.sms.service.SmsService;
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
public class SmsController implements SmsApi {

    private final SmsService smsService;

    //인증 코드 sms 전송
    @Override
    @PostMapping("/sms/authentication")
    public ApiResponse sendAuthenticationCode(@RequestBody @Valid SmsAuthenticationRequest request) {
        log.info("인증 코드 메시지 전송 요청");

        smsService.sendAuthenticationMessage(request.getPhoneNumber());
        log.info("인증 코드 메시지 전송 요청 완료");
        return ApiResponse.createSuccessResponse(HttpStatus.OK.value());
    }

    @Override
    @PostMapping("/sms/verification")
    public ApiResponse<SmsVerificationResponse> verifyAuthenticationCode(
            @RequestBody @Valid SmsVerificationRequest request) {
        log.info("인증 코드 검증 요청");
        SmsVerificationResponse smsVerificationResponse = smsService.verifyAuthenticationCodeAndGetSmsConfirmationToken(
                request.getPhoneNumber(),
                request.getAuthenticationCode()
        );

        log.info("인증 코드 검증 요청 완료");
        return ApiResponse.createSuccessResponseWithData(HttpStatus.ACCEPTED.value(), smsVerificationResponse);
    }

}
