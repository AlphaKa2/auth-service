package com.alphaka.authservice.security.login.user;

import com.alphaka.authservice.dto.Role;
import java.util.Collection;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomUser extends User {

    private String nickname;
    private String profileImage;
    private Role role;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
                      String nickname, String profileImage, Role role) {
        super(username, password, authorities);
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;

    }
}
