package com.green.glampick.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class exclusionSignUpRequestDto {

    private Long ownerId;
    private String exclusionComment;

}
