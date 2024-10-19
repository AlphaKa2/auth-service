package com.alphaka.authservice.dto;

public enum SocialType {
    EMAIL("email"),
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String value;

    SocialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
