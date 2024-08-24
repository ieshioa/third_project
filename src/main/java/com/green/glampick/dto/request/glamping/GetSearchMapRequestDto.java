package com.green.glampick.dto.request.glamping;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetSearchMapRequestDto {
    private String region;
    private String in;
    private String out;
}
