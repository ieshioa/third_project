package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerSignInRequestDto {

    private String ownerEmail;
    private String ownerPw;

}
