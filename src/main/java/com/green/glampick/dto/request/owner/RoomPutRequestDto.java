package com.green.glampick.dto.request.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomPutRequestDto {
    private RoomPostRequestDto requestDto;
    @Schema(example = "1")
    private long roomId;
    @Schema(example = "[100, 101, 102]")
    private List<Long> removeImg;
}
