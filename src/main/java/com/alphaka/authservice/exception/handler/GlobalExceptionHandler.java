package com.alphaka.authservice.exception.handler;

import com.alphaka.authservice.dto.response.ErrorResponse;
import com.alphaka.authservice.exception.ErrorCode;
import com.alphaka.authservice.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ErrorResponse errorResponse = new ErrorResponse(
                errorCode.status(),
                errorCode.code(),
                errorCode.message()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.status()));
    }
}
