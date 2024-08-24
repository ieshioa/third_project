package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.green.glampick.common.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OwnerSignUpRequestDto {

    @JsonIgnore private long ownerId;
    private String businessNumber;
    private String ownerEmail;
    private String ownerPw;
    private String ownerName;
    private String ownerPhone;
    @JsonIgnore private String businessPaperImage;
    @JsonIgnore private int glampingStatus;
    @JsonIgnore private Role role;
    @JsonIgnore private int activateStatus;

}
