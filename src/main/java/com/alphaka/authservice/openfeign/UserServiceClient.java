package com.alphaka.authservice.openfeign;

import com.alphaka.authservice.dto.request.UserSignInRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.dto.request.OAuth2SignInRequest;
import com.alphaka.authservice.dto.response.UserSignInResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @PostMapping("/users/oauth2/signin")
    ApiResponse<UserSignInResponse> oauth2SignIn(@RequestBody OAuth2SignInRequest oAuth2SignInRequest);

    @PostMapping("/users/signin")
    ApiResponse<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest);

    @GetMapping("/users/{userId}")
    ApiResponse<UserSignInResponse> user(@PathVariable("userId") Long userId);
}
