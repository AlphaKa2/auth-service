package com.alphaka.authservice.security.oauth2.user;

import com.alphaka.authservice.dto.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private Long id;
    private String nickname;
    private String profileImage;
    private Role role;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, Long id, String nickname,
                            String profileImage, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
    }
}
