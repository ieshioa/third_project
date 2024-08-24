package com.green.glampick.oauth2.userinfo;

import com.green.glampick.security.SignInProviderType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OAuth2UserInfoFactory {

    public OAuth2UserInfo getOAuth2UserInfo(SignInProviderType signInProviderType
            , Map<String, Object> attributes) {
        return switch(signInProviderType) {
            case GOOGLE -> new GoogleOAuth2UserInfo(attributes);
            case KAKAO -> new KakaoOAuth2UserInfo(attributes);
            case NAVER -> new NaverOAuth2UserInfo(attributes);
            default -> throw new RuntimeException("제공하지 않는 로그인 방식입니다.");
        };
    }

}
