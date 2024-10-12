package com.alphaka.authservice.exception.custom;

import com.alphaka.authservice.exception.ErrorCode;

public class SmsVerificationFailureException extends CustomException {

    public SmsVerificationFailureException() {
        super(ErrorCode.SMS_VERIFICATION_FAILURE);
    }
}
