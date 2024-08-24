package com.green.glampick.dto.response.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearchOwnerPwRequestDto {

    private String ownerName;
    private String ownerEmail;
    private String ownerPw;

}
