package com.alphaka.authservice.security.oauth2.service;

import com.alphaka.authservice.openfeign.UserServiceClient;
import com.alphaka.authservice.dto.SocialType;
import com.alphaka.authservice.dto.request.OAuth2SignInRequest;
import com.alphaka.authservice.dto.response.UserSignInResponse;
import com.alphaka.authservice.security.oauth2.user.CustomOAuth2User;
import com.alphaka.authservice.security.oauth2.user.CustomOAuth2UserDto;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserServiceClient userServiceClient;
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegatingOAuth2UserService = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        OAuth2User oAuth2User = delegatingOAuth2UserService.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String userNameAttributeName = getUserNameAttributeName(userRequest);

        CustomOAuth2UserDto customOAuth2UserDto = new CustomOAuth2UserDto(socialType, attributes);
        UserSignInResponse user = getUser(customOAuth2UserDto);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue())),
                attributes,
                userNameAttributeName,
                user.getId(),
                user.getNickname(),
                user.getProfileImage(),
                user.getRole()
        );
    }

    private UserSignInResponse getUser(CustomOAuth2UserDto customOAuth2UserDto) {
        return userServiceClient.oauth2SignIn(
                OAuth2SignInRequest.from(customOAuth2UserDto)).getData();
    }

    private SocialType getSocialType(String registrationId) {
        if (registrationId.equals(SocialType.GOOGLE.getValue())) {
            return SocialType.GOOGLE;
        }
        return null;
    }

    private static String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
    }
}
