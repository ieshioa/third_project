package com.green.glampick.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostBannerRequestDto {

    @JsonIgnore private Long bannerId;

    private List<String> bannerUrl;

}
