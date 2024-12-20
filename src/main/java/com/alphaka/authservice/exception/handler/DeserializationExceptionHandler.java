package com.alphaka.authservice.exception.handler;

import com.alphaka.authservice.dto.response.ErrorResponse;
import com.alphaka.authservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class DeserializationExceptionHandler {


    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        log.error("요청으로부터 메시지를 읽을 수 없습니다.{}", ex.getMessage());

        ErrorCode errorCode = ErrorCode.DESERIALIZATION_FAILURE;
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.status(),
                errorCode.code(),
                errorCode.message()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.status()));
    }
}
