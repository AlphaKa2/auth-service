package com.alphaka.authservice.security.login.service;

import com.alphaka.authservice.openfeign.UserServiceClient;
import com.alphaka.authservice.dto.request.UserSignInRequest;
import com.alphaka.authservice.dto.response.UserSignInResponse;
import com.alphaka.authservice.security.login.user.CustomUser;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        return new CustomUser(String.valueOf(response.getId()), response.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(response.getRole().getValue())),
                response.getNickname(),
                response.getProfileImage(),
                response.getRole()
                );
    }
}
