package com.green.glampick.dto.request.login;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminSignInRequestDto {
    @Schema(example = "thisisadmin")
    private String adminId;
    @Schema(example = "green502!")
    private String adminPw;

}
