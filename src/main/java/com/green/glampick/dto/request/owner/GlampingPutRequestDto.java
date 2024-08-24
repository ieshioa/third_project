package com.green.glampick.dto.request.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GlampingPutRequestDto {
    private GlampingPostRequestDto requestDto;
    @Schema(example = "1")
    private long glampId;
}
