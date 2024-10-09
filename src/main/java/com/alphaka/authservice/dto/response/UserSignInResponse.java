package com.alphaka.authservice.dto.response;

import com.alphaka.authservice.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignInResponse {

    private String email;
    private Role role;
}