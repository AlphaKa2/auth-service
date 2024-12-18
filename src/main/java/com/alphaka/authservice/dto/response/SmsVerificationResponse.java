package com.alphaka.authservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SmsVerificationResponse {
    private String smsConfirmation;
}
