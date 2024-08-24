package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUserPasswordRequestDto {

    @JsonIgnore private long userId;
    private String userPw;

}
