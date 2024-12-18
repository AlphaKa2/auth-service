package com.alphaka.authservice.exception;

import org.springframework.http.HttpStatus;

public record ErrorCode(int status, String code, String message) {

    public static final ErrorCode INVALID_REFRESH_TOKEN =
            new ErrorCode(HttpStatus.UNAUTHORIZED.value(), "USR017", "유효하지 않은 토큰입니다.");
    public static final ErrorCode SMS_VERIFICATION_FAILURE =
            new ErrorCode(HttpStatus.BAD_REQUEST.value(), "USR004", "인증번호가 일치하지 않습니다.");

    public static final ErrorCode INVALID_EMAIL_OR_PASSWORD =
            new ErrorCode(HttpStatus.UNAUTHORIZED.value(), "USR013", "이메일 혹은 비밀번호가 일치하지 않습니다.");

    public static final ErrorCode DESERIALIZATION_FAILURE =
            new ErrorCode(HttpStatus.BAD_REQUEST.value(), "USR009", "읽을 수 없는 요청입니다.");

    public static final ErrorCode AUTHENTICATION_SERVICE_FAILURE =
            new ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value(), "SYS001", "서버에서 문제가 발생했습니다.");
}
