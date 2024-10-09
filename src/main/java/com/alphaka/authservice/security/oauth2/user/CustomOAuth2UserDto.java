package com.alphaka.authservice.security.oauth2.user;

import com.alphaka.authservice.dto.SocialType;
import java.time.LocalDate;
import java.util.Map;
import lombok.Getter;

@Getter
public class CustomOAuth2UserDto {

    private SocialType socialType;
    private String email;
    private String nickname;
    private String name;
    private String profileImageUrl;
    private LocalDate birth;

    public CustomOAuth2UserDto(SocialType socialType, Map<String, Object> attributes) {
        if (socialType == SocialType.GOOGLE) {
            initializeUserFromGoogle(attributes);
        }
    }

    private void initializeUserFromGoogle(Map<String, Object> attributes) {
        this.socialType = SocialType.GOOGLE;
        this.email = (String) attributes.get("email");
        this.nickname = (String) attributes.get("given_name");
        this.profileImageUrl = (String) attributes.get("picture");
        this.name = (String) attributes.get("name");
        this.birth = null;
    }
}