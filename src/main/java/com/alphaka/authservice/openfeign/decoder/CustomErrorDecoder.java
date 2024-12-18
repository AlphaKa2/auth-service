package com.alphaka.authservice.openfeign.decoder;

import com.alphaka.authservice.exception.ErrorCode;
import com.alphaka.authservice.exception.custom.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    // 오픈페인 요청 응답이 왔을 때 상태코드가 4XX, 5XX일 때 동작
    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        log.error("오픈페인 요청 중 오류가 발생했습니다.");
        log.error("오류 response.stataus: {}", status);

        // 존재하지 않는 사용자
        if (status == 404) {
            return new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }

        return new CustomException(ErrorCode.AUTHENTICATION_SERVICE_FAILURE);
    }

}
