package com.alphaka.authservice.security.login.service;

import com.alphaka.authservice.client.UserServiceClient;
import com.alphaka.authservice.dto.request.UserSignInRequest;
import com.alphaka.authservice.dto.response.ApiResponse;
import com.alphaka.authservice.dto.response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final UserServiceClient userServiceClient;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserSignInResponse response = userServiceClient.signIn(
                new UserSignInRequest(email, null)).getData();

        if (response.getEmail() == null) {
            throw new UsernameNotFoundException("해당 이메일의 유저가 존재하지 않습니다.");
        }

        return User.builder()
                .username(response.getEmail())
                .roles(response.getRole().getValue())
                .password(response.getPassword())
                .build();
    }
}
