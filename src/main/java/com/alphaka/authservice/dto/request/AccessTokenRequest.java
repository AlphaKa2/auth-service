package com.alphaka.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenRequest {

    @NotBlank(message = "accessToken은 필수 입력값입니다.")
    @Schema(description = "블랙리스트인지 검사할 accessToken", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImlkIjozLCJuaWNrbmFtZSI6ImltVGhpcmQiLCJwcm9maWxlIjoiL2ltZy9kZWZhdWx0Iiwicm9sZSI6IlVTRVIiLCJleHAiOjE3MzI3MTg4MzN9._imLEKdTTVCLjrjzj0sfYAJBH8XXmu4eTIZfW2ZNWMGhAvNgzHb8OUaGAdKQhxUQ72rDBX6aj6_4CNzDmEazUQ", requiredMode = RequiredMode.REQUIRED)
    String accessToken;
}
