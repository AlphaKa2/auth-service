package com.alphaka.authservice.openfeign.decoder;

import com.alphaka.authservice.dto.response.ErrorResponse;
import com.alphaka.authservice.exception.ErrorCode;
import com.alphaka.authservice.exception.custom.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        try {
            log.error("오픈페인 요청 중 오류가 발생했습니다.");
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

            return new CustomException(new ErrorCode(errorResponse.getStatus(), errorResponse.getCode(),
                    errorResponse.getMessage()));
        } catch (IOException e) {
            //역직렬화 실패
            throw new CustomException(new ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "", "오류가 발생했습니다."));
        }

    }
}
