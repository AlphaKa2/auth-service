package com.alphaka.authservice.sms.controller;

import com.alphaka.authservice.dto.request.SmsAuthenticationRequest;
import com.alphaka.authservice.dto.request.SmsVerificationRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.dto.response.SmsVerificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;


@Tag(name = "인증 API", description = "인증 관련 API")
public interface SmsApi {

    @Operation(
            summary = "인증번호 전송",
            description = "사용자가 회원가입을 위해 입력한 전화번호로 인증번호를 담은 SMS 메시지를 보내는 API입니다.",
            tags = {"External API"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "인증번호 전송 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "정상 응답",
                                    value = """
                                            {
                                                "status": 200
                                            }
                                            """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "검증 실패",
                                            value = """
                                                    {
                                                        "status": 400,
                                                        "code": "USR009",
                                                        "message": "phoneNumber:전화번호는 10~11자리 숫자만 입력 가능합니다.\\n"       
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "역직렬화 실패",
                                            value = """
                                                    {
                                                        "status": 400,
                                                         "code": "USR009",
                                                         "message": "읽을 수 없는 요청입니다."
                                                    }
                                                    """
                                    )
                            }

                    )
            )
    })
    ApiResponse sendAuthenticationCode(@RequestBody @Valid SmsAuthenticationRequest request);

    @Operation(
            summary = "인증번호 검증",
            description = "사용자가 입력한 인증번호가 유효한지 검증을 요청하는 API입니다. ",
            tags = {"External API"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "인증번호 검증 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "정상 응답",
                                            value = """
                                                    {
                                                        "status": 202,
                                                        "data": {
                                                            "smsConfirmation" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJTbXNDb25maXJtYXRpb24iLCJleHAiOjE3MzM0NjI4MjMsInBob25lTnVtYmVyIjoiMDEwMDAwMDAwMDAifQ.8Lur3HULIkGPJeqZOB1cqduqpp8DeFbCoYE3GCiv31qTV_GrOr6oi7NerhaWXkslcNKJIdECJQtWDdlw2vQmgw"
                                                        }
                                                    }
                                                    """
                                    ),

                            }
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "파라미터 검증 실패",
                                            value = """
                                                    {
                                                        "status": 400,
                                                        "code": "USR009",
                                                        "message": "인증 코드는 6자리 숫자여야 합니다.\\n"       
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "역직렬화 실패",
                                            value = """
                                                    {
                                                        "status": 400,
                                                         "code": "USR009",
                                                         "message": "읽을 수 없는 요청입니다."
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "잘못된 인증 코드",
                                            value = """
                                                    {
                                                        "status": 400,
                                                         "code": "USR004",
                                                         "message": "인증번호가 일치하지 않습니다."
                                                    }
                                                    """
                                    )
                            }

                    )
            )
    })
    ApiResponse<SmsVerificationResponse> verifyAuthenticationCode(
            @RequestBody @Valid SmsVerificationRequest request);

}
