package com.alphaka.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class SmsVerificationRequest {

    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10~11자리 숫자만 입력 가능합니다.")
    @NotBlank(message = "전화번호는 필수 입력값입니다.")
    @Schema(description = "사용자의 전화번호", example = "01012345678")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{6}$", message = "인증 코드는 6자리 숫자여야 합니다.")
    @NotBlank(message = "인증 코드는 필수 입력값입니다.")
    @Schema(description = "인증 코드", example = "167321")
    private String authenticationCode;

}