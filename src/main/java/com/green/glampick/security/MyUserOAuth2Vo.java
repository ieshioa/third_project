package com.green.glampick.security;

import com.green.glampick.common.Role;
import lombok.Getter;

@Getter
public class MyUserOAuth2Vo extends MyUser {

    private final String userEmail;

    public MyUserOAuth2Vo(long userId, Role role, String userEmail) {
        super(userId, role);
        this.userEmail = userEmail;
    }

}
