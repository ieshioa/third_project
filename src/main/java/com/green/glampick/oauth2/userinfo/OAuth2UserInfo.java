package com.green.glampick.oauth2.userinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public abstract class OAuth2UserInfo {

    protected final Map<String, Object> attributes;

    public abstract String getId(); //유니크값 리턴
    public abstract String getName(); //이름
    public abstract String getEmail(); //이메일
    public abstract String getProfilePicUrl(); //프로필 사진   https://..... or null
}
