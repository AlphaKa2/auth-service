package com.alphaka.authservice.controller;

import com.alphaka.authservice.dto.request.AccessTokenRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증 API", description = "인증 관련 API")
public interface AuthApi {

    @Operation(
            summary = "블랙리스트 검증",
            description = "사용자의 accessToken이 블랙리스트에 포함되어 있는지 확인하는 API입니다. 블랙리스트라면 true값이 포함됩니다.",
            tags = {"Internal API"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "블랙리스트 검증 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccessTokenRequest.class)
                    )
            )

    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "인증번호 검증 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {@ExampleObject(
                                    name = "정상 응답-블랙리스트가 아닌 accessToken",
                                    value = """
                                            {
                                                "status": 200,
                                                "data": false
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "정상 응답-블랙리스트인 accessToken",
                                    value = """
                                            {
                                                "status": 200,
                                                "data": true
                                            }
                                            """
                            )}
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
                                                        "code": "USR-009",
                                                        "message": "accessToken은 필수 입력값입니다.\\n"       
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
    ApiResponse<Boolean> blacklist(@RequestBody @Valid AccessTokenRequest request);
}
