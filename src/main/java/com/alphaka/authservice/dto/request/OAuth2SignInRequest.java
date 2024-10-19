package com.alphaka.authservice.dto.request;

import com.alphaka.authservice.dto.SocialType;
import com.alphaka.authservice.security.oauth2.user.CustomOAuth2UserDto;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class OAuth2SignInRequest {

    private String email;
    private SocialType socialType;
    private String profileImage;
    private String name;
    private LocalDate birth;
    private String nickname;

    public static OAuth2SignInRequest from(CustomOAuth2UserDto customOAuth2UserDto) {
        return OAuth2SignInRequest.builder()
                .email(customOAuth2UserDto.getEmail())
                .socialType(customOAuth2UserDto.getSocialType())
                .profileImage(customOAuth2UserDto.getProfileImageUrl())
                .name(customOAuth2UserDto.getName())
                .nickname(customOAuth2UserDto.getNickname())
                .birth(customOAuth2UserDto.getBirth())
                .build();
    }
}
