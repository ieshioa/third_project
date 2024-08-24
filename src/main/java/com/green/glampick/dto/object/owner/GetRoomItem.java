package com.green.glampick.dto.object.owner;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class GetRoomItem {

    @Schema(example = "1")
    private Long roomId;
    @Schema(example = "카라반 102호")
    private String roomName;
    @Schema(example = "이미지 주소")
    private String roomImg;

}
