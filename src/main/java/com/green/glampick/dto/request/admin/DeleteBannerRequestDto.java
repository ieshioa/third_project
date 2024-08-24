package com.green.glampick.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteBannerRequestDto {

    @JsonIgnore private Long adminIdx;
    private Long bannerId;

}
