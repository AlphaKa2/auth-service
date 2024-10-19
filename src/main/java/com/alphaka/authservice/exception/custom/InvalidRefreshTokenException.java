package com.alphaka.authservice.exception.custom;

import com.alphaka.authservice.exception.ErrorCode;

public class InvalidRefreshTokenException extends CustomException {

    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
