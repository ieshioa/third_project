package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Role;
import com.green.glampick.security.SignInProviderType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpSnsRequestDto {

    private Long userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private String userNickname;

}
