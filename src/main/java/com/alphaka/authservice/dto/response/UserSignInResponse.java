package com.alphaka.authservice.dto.response;

import com.alphaka.authservice.dto.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResponse {

    private Long id;
    private String profileImage;
    private String nickname;
    private Role role;
    private String password;
}