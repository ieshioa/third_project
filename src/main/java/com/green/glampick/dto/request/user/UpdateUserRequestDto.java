package com.green.glampick.dto.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequestDto {

    @JsonIgnore private long userId;
    private String userNickname;
    private String userPw;
    private String userPhone;

    @JsonIgnore private String userProfileImage;

}
