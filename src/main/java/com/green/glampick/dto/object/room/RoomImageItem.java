package com.green.glampick.dto.object.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RoomImageItem {
    @Schema(example = "객실 PK")
    private long imageId;
    @Schema(example = "객실 이름")
    private String roomImgName;
}
