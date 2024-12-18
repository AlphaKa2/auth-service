package com.alphaka.authservice.exception.custom;

import com.alphaka.authservice.exception.ErrorCode;

public class AuthenticationFailureException extends CustomException {

    public AuthenticationFailureException() {
        super(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
    }
}
