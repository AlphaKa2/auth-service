package com.alphaka.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsAuthenticationRequest {

    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 숫자만 입력 가능합니다.")
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
    private String authenticationCode;

}