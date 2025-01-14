package com.green.glampick.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.green.glampick.common.Role;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyUser {

    private long userId; //로그인한 사용자의 pk값
    private Role role; //사용자 권한, ROLE_권한이름

}