package com.alphaka.authservice.exception;

import org.springframework.http.HttpStatus;

public record ErrorCode(int status, String code, String message) {

    public static final ErrorCode INVALID_REFRESH_TOKEN =
            new ErrorCode(HttpStatus.UNAUTHORIZED.value(), "USR017", "유효하지 않은 토큰입니다.");
    public static final ErrorCode SMS_VERIFICATION_FAILURE
            = new ErrorCode(HttpStatus.BAD_REQUEST.value(), "USR004", "인증번호가 일치하지 않습니다.");

}
