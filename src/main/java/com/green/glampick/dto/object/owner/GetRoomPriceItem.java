package com.green.glampick.dto.object.owner;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetRoomPriceItem {
    private Long roomId;
    private Integer weekdayPrice;
    private Integer weekendPrice;
}
