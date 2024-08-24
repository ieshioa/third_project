package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Role;
import com.green.glampick.security.SignInProviderType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @JsonIgnore private long userId;
    @JsonIgnore private String providerId;
    @JsonIgnore private SignInProviderType userSocialType;
    private String userEmail;
    private String userPw;
    private String userPhone;
    private String userName;
    private String userNickname;
    @JsonIgnore private Role userRole;

}