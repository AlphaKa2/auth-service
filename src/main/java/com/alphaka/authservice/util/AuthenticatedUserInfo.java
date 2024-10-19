package com.alphaka.authservice.util;

import com.alphaka.authservice.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticatedUserInfo {

    private Long id;
    private String profileImage;
    private String nickname;
    private Role role;

}
