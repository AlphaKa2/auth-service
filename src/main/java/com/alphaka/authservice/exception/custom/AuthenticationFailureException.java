package com.alphaka.authservice.exception.custom;

import com.alphaka.authservice.exception.ErrorCode;

public class AuthenticationFailureException extends CustomException {

    public AuthenticationFailureException() {
        super(ErrorCode.AUTHENTICATION_FAILURE);
    }
}
