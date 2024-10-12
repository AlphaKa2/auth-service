package com.alphaka.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ApiResponse<T> {

    private int status;
    private T data;
    private String message;

    public static <T> ApiResponse<T> createSuccessResponse(int status) {
        return ApiResponse.<T>builder()
                .status(status)
                .build();
    }

    public static <T> ApiResponse<T> createSuccessResponseWithData(int status, T data) {
        return ApiResponse.<T>builder()
                .status(status)
                .data(data)
                .build();
    }

}
