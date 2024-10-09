package com.alphaka.authservice.client;

import com.alphaka.authservice.dto.request.UserSignInRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.dto.request.OAuth2SignInRequest;
import com.alphaka.authservice.dto.response.UserSignInResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @PostMapping("/oauth2/users/signin")
    ApiResponse<UserSignInResponse> oauth2SignIn(@RequestBody OAuth2SignInRequest oAuth2SignInRequest);

    @PostMapping("/users/signin")
    ApiResponse<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest);
}
